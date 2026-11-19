package com.xny.system.service.impl;
import com.xny.common.constant.CommonConstants;
import com.xny.system.entity.DataParamConfig;
import com.xny.system.model.dto.ListDeviceDTO;
import com.xny.system.model.vo.ChartVO;
import com.xny.system.model.vo.ItemVO;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xny.common.enums.GeneralParamEnum;
import com.xny.common.enums.ProjectTypeEnum;
import com.xny.common.utils.CTypeUtils;
import com.xny.common.utils.R;
import com.xny.common.utils.social.SocialContributionUtil;
import com.xny.common.utils.social.entity.SocialContributionData;
import com.xny.system.entity.SysProject;
import com.xny.system.mapper.DataAnalysisMapper;
import com.xny.system.mapper.SysUserMenuMapper;
import com.xny.system.model.dto.ListProjectDTO;
import com.xny.system.model.dto.MapDataCountDTO;
import com.xny.system.model.dto.MoBileHomePageDTO;
import com.xny.system.model.vo.*;
import com.xny.system.service.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Struct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IDataMapServiceImpl implements IDataMapService {
    @Resource
    private DataAnalysisMapper dataAnalysisMapper;

    @Resource
    private ISysProjectService sysProjectService;

    @Resource
    private ISysDeviceService sysDeviceService;

    @Resource
    private SysUserMenuMapper sysUserMenuMapper;

    @Resource
    private IDataParamConfigService dataParamConfigService;

    @Override
    public MapDataCountVO count(MapDataCountDTO dto) {
        //项目权限
        List<Long> projectIdList = sysUserMenuMapper.getProjectIdListHavePerms(StpUtil.getLoginIdAsLong());
        dto.setProjectIds(CollectionUtil.isEmpty(projectIdList) ? "" : StrUtil.join(",",projectIdList));

        //储能项目
        if(null != dto.getProjectTypeId() && dto.getProjectTypeId().equals(ProjectTypeEnum.CNXM.getId())){
            MapStoredEnergyVO storedEnergyVO = new MapStoredEnergyVO();
            storedEnergyVO.setChargeToday(this.otherCount(dto, GeneralParamEnum.JRCD));
            storedEnergyVO.setCurrentDischarge(this.otherCount(dto, GeneralParamEnum.JRFD));
            storedEnergyVO.setTotalCharge(this.otherCount(dto, GeneralParamEnum.TCD));
            storedEnergyVO.setTotalDischarge(this.otherCount(dto, GeneralParamEnum.TFD));
            this.setDevOpsInfo(dto, storedEnergyVO,false);
            return storedEnergyVO;
        }
        //充电项目
        else if(null != dto.getProjectTypeId() && dto.getProjectTypeId().equals(ProjectTypeEnum.CDXM.getId())){
            MapChcChargeVO mapChcChargeVO = new MapChcChargeVO();
            mapChcChargeVO.setTotalRevenue(this.otherCount(dto, GeneralParamEnum.ZSY));
            mapChcChargeVO.setTotalCharge(this.otherCount(dto, GeneralParamEnum.TCD));
            mapChcChargeVO.setTotalChargeTime(this.otherCount(dto, GeneralParamEnum.ZCT));
            mapChcChargeVO.setTotalChargeTimes(this.otherCount(dto, GeneralParamEnum.ZCTS));
            this.setDevOpsInfo(dto, mapChcChargeVO,false);
            return mapChcChargeVO;
        }
        //其他项目
        else {
            boolean flag = false;

            if(null == dto.getProjectTypeId()){
                dto.setProjectTypeIdList(Arrays.asList(
                        ProjectTypeEnum.FBSGF.getId(),ProjectTypeEnum.JZSGF.getId(),
                        ProjectTypeEnum.FDXM.getId()
                ));
                flag = true;
            }

            if(null == dto.getProjectTypeId()){
                //总发电
                ItemVO itemVO = this.otherCount(dto, GeneralParamEnum.ZFDL);
                ItemVO tempItem = itemVO.getChilds().get(0);
                ItemVO temp1Item = itemVO.getChilds().get(1);
                tempItem.setValue(new BigDecimal(tempItem.getValue()).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                temp1Item.setValue(new BigDecimal(temp1Item.getValue()).add(BigDecimal.valueOf(CommonConstants.totalFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                itemVO.setUnit("万kWh");
                itemVO.setValue(new BigDecimal(tempItem.getValue()).add(new BigDecimal(itemVO.getChilds().get(1).getValue())).stripTrailingZeros().toPlainString());

                //总装机
                ItemVO itemVO1 = this.otherCount(dto, GeneralParamEnum.ZZJRL);
                ItemVO tempItem1 = itemVO1.getChilds().get(0);
                tempItem1.setValue(new BigDecimal(tempItem1.getValue()).divide(BigDecimal.valueOf(1000),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                itemVO1.setUnit("MW");
                itemVO1.setValue(new BigDecimal(tempItem1.getValue()).add(new BigDecimal(itemVO1.getChilds().get(1).getValue())).stripTrailingZeros().toPlainString());

                //总收益
                ItemVO itemVO2 = this.otherCount(dto, GeneralParamEnum.ZSY);
                ItemVO tempItem2 = itemVO2.getChilds().get(1);
                tempItem2.setValue(new BigDecimal(itemVO.getChilds().get(1).getValue()).multiply(BigDecimal.valueOf(0.3545)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                itemVO2.setValue(new BigDecimal(tempItem2.getValue()).add(new BigDecimal(itemVO2.getChilds().get(0).getValue())).stripTrailingZeros().toPlainString());

                //总上网

                //ItemVO itemVO3 = this.otherCount(dto, GeneralParamEnum.ZSW);
                ItemVO data1 = this.otherCount(dto, GeneralParamEnum.JRZFDL);
                ItemVO data2 = this.otherCount(dto, GeneralParamEnum.BYZFDL);
                ItemVO data11 = data1.getChilds().get(0);
                ItemVO data12 = data1.getChilds().get(1);
                ItemVO data21 = data2.getChilds().get(0);
                ItemVO data22 = data2.getChilds().get(1);
                ItemVO itemVO3 = new ItemVO().setLabel("发电数据统计").setUnit("万kWh").setChilds(Arrays.asList(
                        new ItemVO().setLabel("日发电量").setUnit("万kW").setValue(new BigDecimal(data11.getValue()).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(data12.getValue())).add(BigDecimal.valueOf(CommonConstants.todayFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()),
                        new ItemVO().setLabel("月发电量").setUnit("万kW").setValue(new BigDecimal(data21.getValue()).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(data22.getValue())).add(BigDecimal.valueOf(CommonConstants.monthFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
                ));

                //社会效益
                ItemVO itemVO4 = this.socialBenefit(strToDouble(itemVO.getValue()));
                itemVO4.getChilds().get(0).setUnit("万吨");
                itemVO4.getChilds().get(1).setUnit("万吨");

                MapPowerPlantVO powerPlantVO = new MapPowerPlantVO();
                powerPlantVO.setTotalItems(this.totalItems(dto));
                powerPlantVO.setGrossGeneration(itemVO);
                powerPlantVO.setGrossCapacity(itemVO1);
                powerPlantVO.setTotalRevenue(itemVO2);
                powerPlantVO.setTotalAccess(itemVO3);
                powerPlantVO.setSocialBenefit(itemVO4);
                powerPlantVO.setChartList(getChartVoList(powerPlantVO,dto));

                this.setDevOpsInfo(dto, powerPlantVO,flag);

                //安全发电
                powerPlantVO.getDevOpsData().getChilds().get(1).setValue(new BigDecimal(itemVO.getValue()).multiply(BigDecimal.valueOf(10)).divide(new BigDecimal(itemVO1.getValue()),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());

                return powerPlantVO;
            }else {
                MapPowerPlantVO powerPlantVO = new MapPowerPlantVO();
                powerPlantVO.setTotalItems(this.totalItems(dto));
                powerPlantVO.setGrossGeneration(this.otherCount(dto, GeneralParamEnum.ZFDL));
                powerPlantVO.setGrossCapacity(this.otherCount(dto,GeneralParamEnum.ZZJRL));
                powerPlantVO.setTotalRevenue(this.otherCount(dto,GeneralParamEnum.ZSY));
                powerPlantVO.setTotalAccess(this.otherCount(dto,GeneralParamEnum.ZSW));
                powerPlantVO.setChartList(getChartVoList(powerPlantVO,dto));

                if(dto.getProjectTypeId().equals(ProjectTypeEnum.JZSGF.getId())){
                    String value = powerPlantVO.getGrossGeneration().getValue();
                    powerPlantVO.getGrossGeneration().setUnit("万kWh").setValue(new BigDecimal(value).add(BigDecimal.valueOf(CommonConstants.totalFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                    powerPlantVO.getGrossCapacity().setUnit("MW");
                    powerPlantVO.getTotalRevenue().setValue(new BigDecimal(powerPlantVO.getGrossGeneration().getValue()).multiply(BigDecimal.valueOf(0.3545)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                    powerPlantVO.setTotalAccess(null);
                }

                powerPlantVO.setSocialBenefit(this.socialBenefit(strToDouble(powerPlantVO.getGrossGeneration().getValue())));


                this.setDevOpsInfo(dto, powerPlantVO,flag);

                //安全发电
                if(dto.getProjectTypeId().equals(ProjectTypeEnum.JZSGF.getId())){
                    powerPlantVO.getSocialBenefit().getChilds().get(0).setUnit("万吨");
                    powerPlantVO.getSocialBenefit().getChilds().get(1).setUnit("万吨");
                    powerPlantVO.getDevOpsData().getChilds().get(1).setValue(new BigDecimal(powerPlantVO.getGrossGeneration().getValue()).multiply(BigDecimal.valueOf(10)).divide(new BigDecimal(powerPlantVO.getGrossCapacity().getValue()),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                }else if(dto.getProjectTypeId().equals(ProjectTypeEnum.FBSGF.getId())){
                    powerPlantVO.getDevOpsData().getChilds().get(1).setValue(new BigDecimal(powerPlantVO.getGrossGeneration().getValue()).divide(new BigDecimal(powerPlantVO.getGrossCapacity().getValue()),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                }

                return powerPlantVO;
            }

        }

    }

    /**
     * 设置运维数据
     * @param dto
     * @param mapDataCountVO
     */
    private void setDevOpsInfo(MapDataCountDTO dto, MapDataCountVO mapDataCountVO,boolean flag) {
        if(null != dto.getProjectTypeId()){
            dto.setProjectTypeIdList(Collections.singletonList(dto.getProjectTypeId()));
        }else {
            dto.setProjectTypeIdList(Arrays.asList(
                    ProjectTypeEnum.FBSGF.getId(),ProjectTypeEnum.JZSGF.getId(),
                    ProjectTypeEnum.FDXM.getId(),ProjectTypeEnum.CNXM.getId(),
                    ProjectTypeEnum.CDXM.getId()
            ));
        }

        mapDataCountVO.setAlarmData(sysDeviceService.ruleParam(
                Arrays.asList(100023L,100024L),
                Arrays.asList("今日告警","累计告警"),
                dto.getProjectTypeIdList(),
                dto.getProjectIds(),
                null
        ));


        ItemVO itemVO1 = sysDeviceService.ruleParam(
                Arrays.asList(100017L, 100018L, 100020L),
                Arrays.asList("安全生产", "安全发电", "设备完好率"),
                Collections.singletonList(null != dto.getProjectTypeId() ? dto.getProjectTypeId() : ProjectTypeEnum.FBSGF.getId())
        );

        //首页 -> 安全生产:有配置取配置的最大值; 安全发电:各项目安全发电总和; 设备完好率:各项目平均值;
        if(flag){
            extracted(itemVO1);
        }

        ItemVO  tempItemVO2 = sysDeviceService.ruleParam(
                Arrays.asList(100019L, 100021L, 100022L),
                Arrays.asList( "安全设备数量","正常运行设备数", "异常设备数"),
                dto.getProjectTypeIdList(),
                dto.getProjectIds(),
                null
        );

        ItemVO  itemVO = new ItemVO().setChilds(new ArrayList<>(
                Arrays.asList(
                        itemVO1.getChilds().get(0),
                        itemVO1.getChilds().get(1),
                        tempItemVO2.getChilds().get(0),
                        itemVO1.getChilds().get(2),
                        tempItemVO2.getChilds().get(1),
                        tempItemVO2.getChilds().get(2)
                )
        )).setLabel(CTypeUtils.ifPc() ? "运维管理":"");

        //pc端要把 "正常运行设备数" 和 "异常设备数" 分开
        if(CTypeUtils.ifPc()){
            mapDataCountVO.setRunStatus(new ItemVO().setChilds(
                    Arrays.asList(itemVO.getChilds().get(4),itemVO.getChilds().get(5))
            ).setLabel("运行状况分析"));
            itemVO.getChilds().remove(5);
            itemVO.getChilds().remove(4);
        }

        mapDataCountVO.setDevOpsData(itemVO);
    }

    private void extracted(ItemVO itemVO1) {
        ItemVO itemVO2 = sysDeviceService.ruleParam(
                Arrays.asList(100017L, 100018L,100020L),
                Arrays.asList("安全生产", "安全发电","设备完好率"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
        );
        ItemVO itemVO3 = sysDeviceService.ruleParam(
                Arrays.asList(100017L, 100018L,100020L),
                Arrays.asList("安全生产", "安全发电","设备完好率"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        );
        ItemVO itemVO4 = sysDeviceService.ruleParam(
                Arrays.asList(100017L, 100018L,100020L),
                Arrays.asList("安全生产", "安全发电","设备完好率"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        );
        ItemVO itemVO5 = sysDeviceService.ruleParam(
                Arrays.asList(100017L, 100018L,100020L),
                Arrays.asList("安全生产", "安全发电","设备完好率"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId())
        );

        itemVO1.getChilds().get(0).setValue(
                Stream.of(
                        Double.valueOf(itemVO1.getChilds().get(0).getValue()),
                        Double.valueOf(itemVO2.getChilds().get(0).getValue()),
                        Double.valueOf(itemVO3.getChilds().get(0).getValue()),
                        Double.valueOf(itemVO4.getChilds().get(0).getValue()),
                        Double.valueOf(itemVO5.getChilds().get(0).getValue())
                ).max(Double::compareTo).get() +""
        );

//        itemVO1.getChilds().get(1).setValue(
//                Stream.of(
//                        Double.valueOf(itemVO1.getChilds().get(1).getValue()),
//                        Double.valueOf(itemVO2.getChilds().get(1).getValue()),
//                        Double.valueOf(itemVO3.getChilds().get(1).getValue())
//                ).mapToDouble(Double::doubleValue).sum() +""
//        );

        itemVO1.getChilds().get(2).setValue(
                Stream.of(
                        Double.valueOf(itemVO1.getChilds().get(2).getValue().replace("%","")),
                        Double.valueOf(itemVO2.getChilds().get(2).getValue().replace("%","")),
                        //Double.valueOf(itemVO3.getChilds().get(2).getValue().replace("%","")),
                        Double.valueOf(itemVO4.getChilds().get(2).getValue().replace("%","")),
                        Double.valueOf(itemVO5.getChilds().get(2).getValue().replace("%",""))
                ).mapToDouble(Double::doubleValue).average().getAsDouble() +"%"
        );
    }


    /**
     * 获取图表列表
     * @param powerPlantVO
     * @param dto
     * @return
     */
    private List<ChartVO> getChartVoList(MapPowerPlantVO powerPlantVO,MapDataCountDTO dto){
        List<ChartVO> chartVOList =new ArrayList<>();

        if(null != dto.getProjectTypeId()){
            chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.getById(dto.getProjectTypeId())));
        }else {
            chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.FDXM));
            //chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.SDXM));
            chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.CDXM));
            chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.CNXM));
            chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.JZSGF));
            chartVOList.add(getChartVO(powerPlantVO,ProjectTypeEnum.FBSGF));
        }

        return chartVOList;
    }

    /**
     * 获取图表
     * @param powerPlantVO
     * @param fdxm
     * @return
     */
    private ChartVO getChartVO(MapPowerPlantVO powerPlantVO,ProjectTypeEnum fdxm){
        Map<String, String> totalItemsMap = powerPlantVO.getTotalItems().getChilds().stream().collect(Collectors.toMap(ItemVO::getLabel, ItemVO::getValue));
        Map<String, String> grossCapacityMap  = powerPlantVO.getGrossCapacity().getChilds().stream().collect(Collectors.toMap(ItemVO::getLabel, ItemVO::getValue));
        Map<String, String> grossGenerationMap  = powerPlantVO.getGrossGeneration().getChilds().stream().collect(Collectors.toMap(ItemVO::getLabel, ItemVO::getValue));
        Map<String, String> totalAccessMap  = powerPlantVO.getTotalAccess().getChilds().stream().collect(Collectors.toMap(ItemVO::getLabel, ItemVO::getValue));
        Map<String, String> totalRevenueMap  = powerPlantVO.getTotalRevenue().getChilds().stream().collect(Collectors.toMap(ItemVO::getLabel, ItemVO::getValue));

        return new ChartVO()
            .setLabel(fdxm.getRemark())
            .setChilds(
                    Arrays.asList(
                            new ChartVO().setLabel("项目总数").setValue(strToDouble(totalItemsMap.get(fdxm.getRemark()))),
                            new ChartVO().setLabel("总发电量(kWh)").setValue(strToDouble(grossGenerationMap.get(fdxm.getRemark()))),
                            new ChartVO().setLabel("总装机容量(kW)").setValue(strToDouble(grossCapacityMap.get(fdxm.getRemark()))),
                            new ChartVO().setLabel("总收益(万元)").setValue(strToDouble(totalRevenueMap.get(fdxm.getRemark()))),
                            new ChartVO().setLabel("总上网(kWh)").setValue(strToDouble(totalAccessMap.get(fdxm.getRemark())))
                    )
            );
    }

    /**
     * 字符串转double
     * @param str
     * @return
     */
    private Double strToDouble(String str){
        if(StrUtil.isBlank(str)){
            return 0d;
        }

        return Double.valueOf(str);
    }

    @Override
    public List<ListProjectVO> listProject(ListProjectDTO dto) {
        List<Long> projectIdListHavePerms = sysUserMenuMapper.getProjectIdListHavePerms(StpUtil.getLoginIdAsLong());
        if(CollectionUtil.isEmpty(projectIdListHavePerms)){
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysProject> wrapper = new LambdaQueryWrapper<SysProject>()
                .eq(null != dto.getProjectTypeId(), SysProject::getProjectTypeId, dto.getProjectTypeId())
                .eq(StrUtil.isNotBlank(dto.getProvinceCode()), SysProject::getProvinceCode, dto.getProvinceCode())
                .in(CollectionUtil.isNotEmpty(dto.getProjectTypeIdList()), SysProject::getProjectTypeId, dto.getProjectTypeIdList())
                .in(SysProject::getId, projectIdListHavePerms);

        //手机端只返回分布式和集中式光伏
        if(CTypeUtils.ifMobile()){
            wrapper.in(SysProject::getId,1L,2L);
        }

        List<SysProject> list = sysProjectService.list(wrapper);
        List<ListProjectVO> listProjectVOS = CglibUtil.copyList(list, ListProjectVO::new);

        //手机端添加项目维度
        if(CTypeUtils.ifMobile()){
            for (ListProjectVO listProjectVO : listProjectVOS) {
                if(listProjectVO.getId().equals(1L)){
                    String projectDimension = dataAnalysisMapper.projectDimension("1,3,4");
                    listProjectVO.setProjectDimension(projectDimension.replace(",","/").replace("项目",""));
                }else {
                    listProjectVO.setProjectDimension(listProjectVO.getProjectName());
                }

                if(listProjectVO.getId().equals(1L)){
                    listProjectVO.setProjectName("陕西省电力设计院分布式光伏项目");
                }else if(listProjectVO.getId().equals(2L)){
                    listProjectVO.setProjectName("延安市宝塔区农光互补光伏发电项目");
                }
            }
        }

        return listProjectVOS;
    }


    /**
     * 统计项目总数
     * @param dto
     * @return
     */
    @Override
    public ItemVO totalItems(MapDataCountDTO dto){
        List<ProjectNumberVO> list = dataAnalysisMapper.projectNumber(dto);

        ItemVO itemVO = new ItemVO();
        if(null != dto.getProjectTypeId() && CTypeUtils.ifPad() && StrUtil.isNotBlank(dto.getProvinceCode())){
            itemVO.setLabel("项目汇总");
        }else {
            itemVO.setLabel(GeneralParamEnum.XMZS.getRemark());
        }

        itemVO.setIcon(GeneralParamEnum.XMZS.getIcon());
        itemVO.setValue(list.stream().map(ProjectNumberVO::getTotalCount).reduce(0d,Double::sum).toString());

        itemVO.setChilds(list.stream().map(
                temp -> new ItemVO()
                        .setLabel(temp.getProjectTypeName())
                        .setValue(String.valueOf(temp.getTotalCount()))
        ).collect(Collectors.toList()));

        return itemVO;
    }

    /**
     * 社会效益
     * @param pgc 发电量
     * @return
     */
    public ItemVO socialBenefit(Double pgc){
        SocialContributionData data = SocialContributionUtil.getData(null != pgc ? pgc.longValue() : 0L);

        ItemVO itemVO = new ItemVO();
        itemVO.setLabel("社会效益");
        List<ItemVO> childs = new ArrayList<>();
        childs.add(new ItemVO().setLabel("CO<sub>2</sub>减排").setValue(String.valueOf(data.getCo2())).setIcon("1").setUnit("吨"));
        childs.add(new ItemVO().setLabel("标煤节约").setValue(String.valueOf(data.getStandardCoal())).setIcon("2").setUnit("吨"));
        return itemVO.setChilds(childs);
    }

    /**
     * 其他统计
     * @param dto
     * @param paramEnum
     * @return
     */
    @Override
    public ItemVO otherCount(MapDataCountDTO dto ,GeneralParamEnum paramEnum){
        dto.setParamId(paramEnum.getId());
        List<ProjectDataCountVO> list = dataAnalysisMapper.projectDataCount(dto);

        ItemVO itemVO = new ItemVO();
        itemVO.setLabel(paramEnum.getRemark());
        itemVO.setUnit(paramEnum.getUnit());
        itemVO.setIcon(paramEnum.getIcon());
        itemVO.setValue(list.stream().map(ProjectDataCountVO::getTotalValue).reduce(0d,Double::sum).toString());

        itemVO.setChilds(list.stream().map(
                temp -> new ItemVO()
                        .setLabel(temp.getProjectTypeName())
                        .setValue(String.valueOf(temp.getTotalValue()))
                        .setIcon(ProjectTypeEnum.getById(temp.getProjectTypeId()).getIcon())
        ).collect(Collectors.toList()));

        return itemVO;
    }


}
