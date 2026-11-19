package com.xny.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xny.common.constant.CommonConstants;
import com.xny.common.enums.ChartTypeEnum;
import com.xny.common.utils.CTypeUtils;
import com.xny.common.utils.CustomUtils;
import com.xny.common.utils.R;
import com.xny.common.utils.tide.TideUtil;
import com.xny.common.utils.tide.entity.TideData;
import com.xny.common.utils.tide.entity.TimeAndLevel;
import com.xny.system.entity.DataParamConfig;
import com.xny.system.entity.DataQdWarn;
import com.xny.system.entity.SysParamSimulate;
import com.xny.system.mapper.*;
import com.xny.system.model.vo.ItemVO;
import cn.hutool.core.util.StrUtil;
import com.xny.common.enums.GeneralParamEnum;
import com.xny.common.enums.ProjectTypeEnum;
import com.xny.common.utils.weather.WeatherUtil;
import com.xny.common.utils.weather.entity.WeatherData;
import com.xny.system.entity.SysDevice;
import com.xny.system.model.dto.*;
import com.xny.system.model.vo.*;
import com.xny.system.service.IDataMapService;
import com.xny.system.service.IDataParamConfigService;
import com.xny.system.service.IDataQdWarnService;
import com.xny.system.service.ISysDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 设备信息表 服务实现类
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
@Service
public class SysDeviceServiceImpl extends ServiceImpl<SysDeviceMapper, SysDevice> implements ISysDeviceService {
    @Resource
    private SysDeviceMapper sysDeviceMapper;

    @Resource
    private IDataMapService dataMapService;

    @Resource
    private WeatherUtil weatherUtil;

    @Resource
    private TideUtil tideUtil;

    @Resource
    private SysParamSimulateMapper sysParamSimulateMapper;

    @Resource
    private SysUserMenuMapper sysUserMenuMapper;

    @Resource
    private DataYkcEquipmentMapper dataYkcEquipmentMapper;

    @Resource
    private DataParamConfigMapper dataParamConfigMapper;

    @Resource
    private IDataParamConfigService dataParamConfigService;

    @Resource
    private IDataQdWarnService dataQdWarnService;

    @Value("${third-party.weather.xian}")
    private String weatherCityCodeXian;

    @Value("${third-party.weather.yanan}")
    private String weatherCityCodeYanan;


    @Override
    public PvGlobalOverviewVO pvGlobalOverview(PvGlobalOverviewDTO dto) {
        PvGlobalOverviewVO globalOverviewVO = new PvGlobalOverviewVO();

        globalOverviewVO.setDeviceList(this.listDevice(dto.setProjectType(ProjectTypeEnum.FBSGF)));

        globalOverviewVO.setEnvDataToday(this.envDataToday(ProjectTypeEnum.FBSGF.getId()).setLabel("今日环境数据"));
        globalOverviewVO.getEnvDataToday().getChilds().remove(0);


        if(CTypeUtils.ifPc()){
            globalOverviewVO.setDataView(this.ruleParam(
                    Arrays.asList(100002L,100003L,100005L,110003L,110041L,110004L),
                    Arrays.asList("今日收益","本月收益","累计收益","今日发电量","本月发电量","累计发电量"),
                    Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
            ).setLabel("数据一览"));
        }else {
            globalOverviewVO.setDataView(this.ruleParam(
                    Arrays.asList(100002L,110003L,100003L,110041L,100005L,110004L),
                    Arrays.asList("今日收益","今日发电量","本月收益","本月发电量","累计收益","累计发电量"),
                    Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
            ).setLabel("数据一览"));
        }

        globalOverviewVO.setOtherStatistics(this.ruleParam(
                Arrays.asList(100008L,110021L,100011L,100009L),
                Arrays.asList("累计上网","总有功功率","节约电费","总装机容量"),
                Arrays.asList(ProjectTypeEnum.FBSGF.getId())
        ));

        globalOverviewVO.setSocialBenefit(dataMapService.socialBenefit(
                globalOverviewVO.getDeviceList().stream().map(ListDeviceVO::getParamValue).reduce(0d, Double::sum)
        ));

        globalOverviewVO.setDevOpsData(this.ruleParam(
                Arrays.asList(100017L, 100018L, 100020L, 100023L),
                Arrays.asList("安全生产", "安全发电", "设备完好率", "今日告警数量"),
                Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
        ).setLabel("运维数据"));

        String totalPower = globalOverviewVO.getDataView().getChilds().get(5).getValue();
        String zzj= globalOverviewVO.getOtherStatistics().getChilds().get(3).getValue();

        globalOverviewVO.getDevOpsData().getChilds().get(1).setValue(
            new BigDecimal(totalPower).divide(new BigDecimal(zzj),2,BigDecimal.ROUND_DOWN).toPlainString()
        );

        List<ChartVO> chartVOList = getChartVOList();
        if(CTypeUtils.ifMobile()){
            globalOverviewVO.setChartList(Arrays.asList(
                    chartVOList.get(0).setLabel("光伏侧-总有功功率"),
                    //chartVOList.get(1).setLabel("光伏侧-光照强度"),
                    chartVOList.get(1).setLabel("光伏侧-日发电量"),
                    chartVOList.get(2).setLabel("储能侧-总有功功率"),
                    new ChartVO().setChilds(Collections.singletonList(chartVOList.get(3).getChilds().get(0))).setLabel("储能侧-日充电量"),
                    new ChartVO().setChilds(Collections.singletonList(chartVOList.get(3).getChilds().get(1))).setLabel("储能侧-日放电量")
            ));
        }else {
            globalOverviewVO.setChartList(chartVOList);
        }

        return globalOverviewVO;
    }

    @Override
    public SystemArchitectureVO systemArchitecture(SystemArchitectureDTO dto) {
        SystemArchitectureVO systemArchitectureVO = new SystemArchitectureVO();

        systemArchitectureVO.setInverterDeviceData(this.ruleParam(
                Arrays.asList(110004L,110003L,110021L,110041L),
                Arrays.asList("累计发电量","今日发电量","总有功功率","本月发电量"),
                Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
        ).setLabel("逆变器设备数据"));


//        ItemVO itemVO = this.ruleParam(
//                Arrays.asList(
//                        120037L, 120075L, 120009L,
//                        120038L, 120076L, 120006L,
//                        120026L, 120077L, 120002L
//                ),
//                Arrays.asList(
//                        "总充电量", "PCS直流电压", "PCS线电压",
//                        "总放电量", "PCS直流电流", "PCS相电流",
//                        "剩余电量", "PCS直流功率", "PCS总有功功率"
//                ),
//                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
//        );

        ItemVO itemVO = this.ruleParam(
                Arrays.asList(
                        100017L, 120002L, 120014L,
                        100020L, 120015L, 120026L,
                        100023L, 120025L, 120080L
                ),
                Arrays.asList(
                        "安全生产", "储能有功功率", "方阵额定功率",
                        "设备完好率", "方阵额定容量", "剩余可放电量",
                        "今日告警数量", "剩余可充电量", "柜体温度"
                ),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        );


        if(CTypeUtils.ifPc()){
            systemArchitectureVO.setStorageDeviceData(new ItemVO()
                    .setChilds(Arrays.asList(
                            itemVO.getChilds().get(0),
                            itemVO.getChilds().get(3),
                            itemVO.getChilds().get(6)
                    )).setLabel("储能设备数据"));

            systemArchitectureVO.setElectricData(new ItemVO()
                    .setChilds(Arrays.asList(
                            itemVO.getChilds().get(1),
                            itemVO.getChilds().get(2),
                            itemVO.getChilds().get(4),
                            itemVO.getChilds().get(5),
                            itemVO.getChilds().get(7),
                            itemVO.getChilds().get(8)
                    )));
        }else {
            systemArchitectureVO.setStorageDeviceData(itemVO.setLabel("储能设备数据"));
        }


        systemArchitectureVO.setRealTimeData(this.realTimeData(
                new ChartDataDTO().setProjectTypeId(ProjectTypeEnum.FBSGF.getId())
                .setParamIdList(Arrays.asList(110018L,110021L,110023L))
                .setParamNameList(Arrays.asList("I(A)","P(kW)","COS"))
                .setIfBpv(dto.isIfBpv())
        ));

        systemArchitectureVO.setStorageStatus(getStatus());

        return systemArchitectureVO;
    }

    @Override
    public InverterMonitoringVO inverterMonitoring(InverterMonitoringDTO dto) {
        InverterMonitoringVO inverterMonitoringVO = new InverterMonitoringVO();
        inverterMonitoringVO.setInverterDataStatistics(this.ruleParam(
                Collections.singletonList(110004L),
                Collections.singletonList("累计发电量"),
                Collections.singletonList(dto.isIfBpv() ? ProjectTypeEnum.JZSGF.getId() : ProjectTypeEnum.FBSGF.getId())
        ).setLabel("逆变器数据统计"));

        inverterMonitoringVO.setOtherStatistics(this.ruleParam(
                Arrays.asList(110003L,110021L, 110041L,110022L),
                Arrays.asList("当日总发电量","总有功功率","当月总发电量","总无功功率"),
                Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
        ));

        inverterMonitoringVO.setChartList(getChartVOList2());

        inverterMonitoringVO.setRealTimeData(this.realTimeData(
                new ChartDataDTO()
                        .setProjectTypeId(ProjectTypeEnum.FBSGF.getId())
                        .setExcludeCustomParam(true)
                    .setParamIdList(Arrays.asList(
                            110093L,110015L,110016L,110017L,
                            110018L,110019L,110020L,110021L,
                            110022L,110014L,110007L,110024L,
                            110023L,110006L, 110003L,110041L,
                            110004L
                    )).setIfBpv(dto.isIfBpv())


        ));
        return inverterMonitoringVO;
    }

    @Override
    public DataStatisticsVO dataStatistics(DataStatisticsDTO dto) {
        DataStatisticsVO dataStatisticsVO = new DataStatisticsVO();
        dataStatisticsVO.setTopDataStatistics(this.ruleParam(
                Arrays.asList(110004L,100009L,100005L,100008L,110021L),
                Arrays.asList("累计发电量","总装机容量","累计收益","累计上网","总有功功率"),
                Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
        ));

        dataStatisticsVO.setChartList(getChartVOList3(dto));

        return dataStatisticsVO;
    }

    /**
     * 图表列表排序
     * @return
     */
    private List<ChartVO> getChartVOList(List<ChartVO> list ,List<Integer> indexList){
        List<ChartVO> chartList = new ArrayList<>();
        for (Integer index : indexList) {
            chartList.add(list.get(index));
        }
        return chartList;
    }

    /**
     * 全局总览图表
     * @return
     */
    private List<ChartVO> getChartVOList() {
        List<ChartVO> chartList = new ArrayList<>();

        //光伏
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("光伏侧数据")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("kW")
                                .setParamId(110021L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );
//        chartList.add(this.getChartVO(new ChartInfoDTO()
//                    .setChartItem(new ChartItemInfoDTO()
//                            .setItemName("光照强度")
//                            .setItemStyle("0")
//                            .setItemUnit("lx")
//                            .setParamId(100010L)
//                            .setProjectType(ProjectTypeEnum.FBSGF)
//                    )
//            )
//        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartItem(new ChartItemInfoDTO()
                                .setIfIncrement(true)
                                .setItemName("日发电量")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );

        //储能
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("储能侧数据")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("kW")
                                .setParamId(120002L)
                                .setProjectType(ProjectTypeEnum.CNXM)
                        )
                )
        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartItem(new ChartItemInfoDTO()
                                .setIfIncrement(true)
                                .setItemName("日充电量")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(120035L)
                                .setProjectType(ProjectTypeEnum.CNXM)
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setIfIncrement(true)
                                .setItemName("日放电量")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(120036L)
                                .setProjectType(ProjectTypeEnum.CNXM)
                        )
                )
        );
        return chartList;
    }



    /**
     * 逆变器监测图表
     * @return
     */
    private List<ChartVO> getChartVOList2() {
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("累计发电量趋势")
                        .setDataType("month")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("发电量")
                                .setItemUnit("kWh")
                                .setItemStyle("0")
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );
        return chartList;
    }

    /**
     * 逆变器监测图表
     * @return
     */
    private List<ChartVO> getChartVOList2_1() {
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("累计发电量趋势")
                        .setDataType("month")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("发电量")
                                .setItemUnit("万kWh")
                                .setItemStyle("0")
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                                .setDeviceIdList(Collections.singletonList(12L))
                        )
                )
        );
        return chartList;
    }


    /**
     * 数据统计图表
     * @return
     */
    private List<ChartVO> getChartVOList3(DataStatisticsDTO dto) {
        List<ChartVO> chartList = new ArrayList<>();

        List<ChartDataVO> list = sysDeviceMapper.listRealTimeData(new ChartDataDTO()
                .setProjectTypeId(ProjectTypeEnum.FBSGF.getId())
                .setParamId(110004L)
        ).stream().map(s->{
            ChartDataVO listHourDataVO = new ChartDataVO();
            listHourDataVO.setItemValue(Double.valueOf(s.getParamValue()));
            listHourDataVO.setItemLable(s.getDeviceName());
            return listHourDataVO;
        }).collect(Collectors.toList());

        chartList.add(this.getChartVO2(new ChartInfoDTO()
                        .setChartName("各逆变器累计发电量占比")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("累计发电量")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setDataList(list)
                        )
                )
        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("上网趋势")
                        .setDataType("month")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("上网")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(100008L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("发电量及增长情况")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("发电量")
                                .setItemUnit("kWh")
                                .setItemStyle("0")
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("同比增长")
                                .setItemStyle("0")
                                .setItemUnit("%")
                                .setIfYoy(true)
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("总有功功率趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("kw")
                                .setParamId(110021L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );

        //辐照度及发电量预测 模拟数据
        SysParamSimulate fdycSimulate = sysParamSimulateMapper.selectById(105L);
        SysParamSimulate fzycSimulate = sysParamSimulateMapper.selectById(106L);

        chartList.add(new ChartVO()
                .setLabel("辐照度及发电量预测")
                .setChilds(Arrays.asList(
                        prediction(new PredictionDTO()
                                .setItemName("辐照度")
                                .setItemStyle("0")
                                .setItemUnit("w/m2")
                                .setParamLowerLimit(fzycSimulate.getParamLowerLimit())
                                .setParamUpperLimit(fzycSimulate.getParamUpperLimit())
                                .setParamBaseValue(fzycSimulate.getParamBaseValue())
                                .setType(dto.getType())
                        ),
                        prediction(new PredictionDTO()
                                .setItemName("发电量")
                                .setItemStyle("0")
                                .setItemUnit("kwh")
                                .setParamLowerLimit(fdycSimulate.getParamLowerLimit())
                                .setParamUpperLimit(fdycSimulate.getParamUpperLimit())
                                .setParamBaseValue(fdycSimulate.getParamBaseValue())
                                .setType(dto.getType())
                        )
                ))
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("收益分析")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("收益")
                                .setItemStyle("0")
                                .setItemUnit("万元")
                                .setParamId(100003L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );
        return chartList;
    }

    @Override
    public DeviceMonitorVO deviceMonitor(DeviceMonitorDTO dto) {
        DeviceMonitorVO deviceMonitorVO = new DeviceMonitorVO();
        deviceMonitorVO.setStorageInfoMonitor(this.ruleParam(
                Arrays.asList(120077L,120075L,120076L,120072L,120073L),
                Arrays.asList("直流母线功率","直流母线电压","直流母线电流","储能系统SOC","储能系统SOH"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("储能信息监测"));

        deviceMonitorVO.setStorageStatusMonitor(this.ruleParam(
                Arrays.asList(
                        170001L,170002L,170003L,170004L,
                        170005L,170006L,170007L,170008L,
                        170009L,170010L,170011L,170012L
                ),
                Arrays.asList(
                        "总有功功率","A相有功功率","B相有功功率","C相有功功率",
                        "总无功功率","A相无功功率","B相无功功率","C相无功功率",
                        "总功率因数","A相功率因数","B相功率因数","C相功率因数"
                ),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("储能状态监视"));

        deviceMonitorVO.setDevOpsData(this.ruleParam(
                Arrays.asList(100017L,100020L,100023L),
                Arrays.asList("安全生产","设备完好率","今日告警数量"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("运维数据"));

        return deviceMonitorVO;
    }

    @Override
    public BatteryMonitorVO batteryMonitor(BatteryMonitorDTO dto) {
        BatteryMonitorVO batteryMonitorVO = new BatteryMonitorVO();

        ItemVO itemVO = this.ruleParam(
                Arrays.asList(120107L, 120037L, 120035L, 120038L, 120036L),
                Arrays.asList("实时状态", "总充电量", "今日充电量", "总放电量", "今日放电量"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        );
        //设置储能设备状态
        itemVO.getChilds().get(0).setValue(getStatus());
        batteryMonitorVO.setTopInfo(itemVO);

        batteryMonitorVO.setRunningMonitor(this.ruleParam(
                Arrays.asList(
                        120002L,120014L,120015L,120026L,120025L,
                        120080L,120083L,120086L,120089L,120092L
                ),
                Arrays.asList(
                        "储能有功功率","方阵额定功率","方阵额定容量","剩余可放电量","剩余可充电量",
                        "柜体温度","最高电压值","最低电压值","最高温度","最低温度"
                ),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("运行监测"));

        batteryMonitorVO.setAbnormalAlarm(this.ruleParam(
                Arrays.asList(100012L,100013L,100014L,100015L,100016L),
                Arrays.asList("PCS通讯故障报警","PCS综合故障报警","BMS综合故障报警","能量块SOC异常","能量块急停报警"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("异常状态分析"));

        batteryMonitorVO.setChartList(getChartVOList4());
        return batteryMonitorVO;
    }

    @Override
    public NewDeviceMonitorVO newDeviceMonitor(DeviceMonitorDTO dto) {
        NewDeviceMonitorVO deviceMonitorVO = new NewDeviceMonitorVO();
        deviceMonitorVO.setStorageInfoMonitor(this.ruleParam(
                Arrays.asList(120077L,120075L,120076L,120072L,120073L),
                Arrays.asList("直流母线功率","直流母线电压","直流母线电流","储能系统SOC","储能系统SOH"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("储能信息监测"));

        deviceMonitorVO.setRunningMonitor(this.ruleParam(
                Arrays.asList(
                        120002L,120014L,120015L,120026L,120025L,
                        120080L,120083L,120086L,120089L,120092L
                ),
                Arrays.asList(
                        "储能有功功率","方阵额定功率","方阵额定容量","剩余可放电量","剩余可充电量",
                        "柜体温度","最高电压值","最低电压值","最高温度","最低温度"
                ),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("运行监测"));

        deviceMonitorVO.setDevOpsData(this.ruleParam(
                Arrays.asList(100017L,100020L,100023L),
                Arrays.asList("安全生产","设备完好率","今日告警数量"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        ).setLabel("运维数据"));

        deviceMonitorVO.setWarnList(getWarnList());

        return deviceMonitorVO;
    }

    /**
     * 获取储能异常信息
     * @return
     */
    private List<List<String>> getWarnList(){
        List<DataQdWarn> list = dataQdWarnService.list(new LambdaQueryWrapper<DataQdWarn>()
                .eq(DataQdWarn::getDelFlag, 0)
                .ne(DataQdWarn::getType,"station")
                .orderByDesc(DataQdWarn::getStartTime)
                .last("limit 10")
        );

        List<List<String>> resultLsit = new ArrayList<>();

        resultLsit.add(Arrays.asList("设备uuid","告警描述","开始时间"));

        for (DataQdWarn dataQdWarn : list) {
            List<String> row =  new ArrayList<>();
            row.add(dataQdWarn.getUuid());


            StringBuilder sbr = new StringBuilder();
            if(dataQdWarn.getType().equals("elink")){
                sbr.append("能量链");
            }else if(dataQdWarn.getType().equals("eblock")){
                sbr.append("能量块");
            }else if (dataQdWarn.getType().equals("meter")){
                sbr.append("电表");
            }
            sbr.append(dataQdWarn.getName());
            if(dataQdWarn.getWarnType()==1){
                sbr.append("离线告警");
            }else {
                sbr.append("⾮计划运⾏警告");
            }
            row.add(sbr.toString());

            row.add(DateUtil.format(dataQdWarn.getStartTime(),"yyyy-MM-dd HH:mm:ss"));

            resultLsit.add(row);
        }
        return resultLsit;
    }

    @Override
    public ChargeStationMonitorVO chargeStationMonitor(ChargeStationMonitorDTO dto) {
        ChargeStationMonitorVO chargeStationMonitorVO = new ChargeStationMonitorVO();

        chargeStationMonitorVO.setStationData(this.ruleParam(
                Arrays.asList(130050L,130051L,130052L),
                Arrays.asList("充电桩总数","使用中","空闲中"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId()))
        .setLabel("充电桩数据"));

        ItemVO itemVO = this.ruleParam(
                Arrays.asList(100005L, 120037L, 100006L, 100007L),
                Arrays.asList( "总收益", "总充电量", "总充电时长", "总充电次数"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId()));

        if(CollectionUtil.isNotEmpty(itemVO.getChilds())){
            itemVO.getChilds().get(0).setUnit("元");
            itemVO.getChilds().get(1).setUnit("kWh");
        }
        chargeStationMonitorVO.setMultipleData(itemVO);

        chargeStationMonitorVO.setInUseData(new ItemVO().setChilds(getYkcEquipmentVOS())
                .setLabel(CTypeUtils.ifMobile() ? "充电桩实时监测":""));

        chargeStationMonitorVO.setDevOpsData(this.ruleParam(
                Arrays.asList(100017L,100020L, 100023L),
                Arrays.asList("安全生产","设备完好率", "今日告警数量"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId())
        ).setLabel("运维数据"));

        chargeStationMonitorVO.setStationStatusData(this.ruleParam(
                Arrays.asList(130054L,130055L,130056L,130057L),
                Arrays.asList("在线率","使用率","良好率","故障率"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId())
        ).setLabel("充电桩状态"));

        chargeStationMonitorVO.setSpearStatusData(this.ruleParam(
                Arrays.asList(130056L,130057L,130054L),
                Arrays.asList("良好率","故障率","在线率"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId())
        ).setLabel("充电枪状态"));

        chargeStationMonitorVO.setChartList(getChartVOList9());

        return chargeStationMonitorVO;
    }

    /**
     * 获取充电桩实时监测数据
     * @return
     */
    private List<ItemVO> getYkcEquipmentVOS() {
        int i = 1;
        List<ItemVO> itemVOList = new ArrayList<>();
        List<YkcEquipmentVO> ykcEquipmentVOS = dataYkcEquipmentMapper.ykcEquipmentList();
        for (YkcEquipmentVO vo : ykcEquipmentVOS) {
            if(i>6){
                break;
            }
            ItemVO item = new ItemVO().setLabel("快充"+i+"号");
            Integer status = vo.getStatus();
            if(status == 0 || status == 2){
                item.setChilds(Collections.singletonList(
                        new ItemVO().setLabel("状态").setValue(status == 0 ? "空闲中":"故障报警")
                ));
            }else {
                item.setChilds(Arrays.asList(
                        new ItemVO().setLabel("状态").setValue("使用中"),
                        new ItemVO().setLabel("电流").setValue(null == vo.getCurrentA() ? "--" : vo.getCurrentA().stripTrailingZeros().toPlainString()).setUnit("A"),
                        new ItemVO().setLabel("电压").setValue(null == vo.getVoltageA() ? "--" : vo.getVoltageA().stripTrailingZeros().toPlainString()).setUnit("V"),
                        new ItemVO().setLabel("充电时长").setValue(null == vo.getStartTime() || null == vo.getEndTime() ? "--" : String.valueOf((vo.getEndTime().getTime()-vo.getStartTime().getTime())/60000)).setUnit("分钟"),
                        new ItemVO().setLabel("费用").setValue(null == vo.getStartTime() ? "--":vo.getTotalMoney().stripTrailingZeros().toPlainString()).setUnit("元")
                ));
            }

            itemVOList.add(item);
            i++;
        }
        return itemVOList;
    }

    private List<ChartVO> getChartVOList9() {
        List<ChartVO> chartList = new ArrayList<>();

        //获取近一周的充电量数据
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("day")
                        .setChartName("近一周充电高峰趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("用电量")
                                .setItemUnit("度")
                                .setParamId(130058L)
                                .setItemStyle("0")
                                .setProjectType(ProjectTypeEnum.CDXM)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("当日电流电压趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("电流")
                                .setItemUnit("A")
                                .setItemStyle("0")
                                .setParamId(130059L)
                                .setProjectType(ProjectTypeEnum.CDXM)
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("电压")
                                .setItemUnit("V")
                                .setItemStyle("0")
                                .setParamId(130060L)
                                .setProjectType(ProjectTypeEnum.CDXM)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("当日功率趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("功率")
                                .setItemUnit("kW")
                                .setItemStyle("0")
                                .setParamId(130061L)
                                .setProjectType(ProjectTypeEnum.CDXM)
                        )
                )
        );

        chartList.add(new ChartVO()
                .setLabel("充电枪状态")
                .setChilds(Collections.singletonList(
                        new ChartVO()
                                .setUnit("")
                                .setLabel("")
                                .setChilds(Arrays.asList(
                                        new ChartVO().setLabel("快充").setValue(1.00d),
                                        new ChartVO().setLabel("慢充").setValue(0.00d)
                                    )
                                )
                        )
                )
        );


        return chartList;

    }

    /**
     * 获取储能设备状态
     * @return
     */
    private String getStatus() {
        ItemVO tempItemVO = this.ruleParam(
                Arrays.asList(120106L, 120107L, 120108L),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        );

        List<ItemVO> childs = tempItemVO.getChilds();
        String status;
        if(childs.get(1).getValue().equals("1")){
            status = "充电中";
        }else if(childs.get(2).getValue().equals("1")){
            status = "放电中";
        }else if(childs.get(0).getValue().equals("1")){
            status = "故障";
        }else {
            status = "待机";
        }
        return status;
    }

    @NotNull
    private List<ChartVO> getChartVOList4() {
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("充放电量分析")
                        .setChartItem(new ChartItemInfoDTO()
                                .setIfIncrement(true)
                                .setItemName("当日充电量")
                                .setItemUnit("kWh")
                                .setItemStyle("0")
                                .setParamId(120035L)
                                .setProjectType(ProjectTypeEnum.CNXM)
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setIfIncrement(true)
                                .setItemName("当日放电量")
                                .setItemUnit("kWh")
                                .setItemStyle("0")
                                .setParamId(120036L)
                                .setProjectType(ProjectTypeEnum.CNXM)
                        )
                )
        );
        return chartList;
    }

    @Override
    public PilesMonitorVO pilesMonitor(PilesMonitorDTO dto) {
        PilesMonitorVO pilesMonitorVO = new PilesMonitorVO();
        pilesMonitorVO.setTopInfo(this.ruleParam(
                Arrays.asList(130032L,130039L,130037L,130031L),
                Arrays.asList("当前状态","通信状态","实时温度","电网频率"),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId())
        ));
        pilesMonitorVO.setRunningMonitor(this.ruleParam(
                Arrays.asList(
                        130023L,130024L,130025L,
                        130026L,130027L,130028L
                ),
                Arrays.asList(
                        "A相电压","B相电压","C相电压",
                        "A相电流","B相电流","C相电流"
                ),
                Collections.singletonList(ProjectTypeEnum.CDXM.getId())
        ).setLabel(CTypeUtils.ifMobile() ? "三相数据":""));
        pilesMonitorVO.setChartList(getChartVOList5());
        return pilesMonitorVO;
    }

    @NotNull
    private List<ChartVO> getChartVOList5() {
        List<ChartVO> chartList = new ArrayList<>();

        List<ChartDataVO> list = sysDeviceMapper.listRealTimeData(new ChartDataDTO()
                .setDeviceId(9L)
                .setParamIdList(Arrays.asList(130012L, 130014L, 130016L, 130018L))
                .setProjectTypeId(ProjectTypeEnum.CDXM.getId())
        ).stream().map(s->{
            ChartDataVO listHourDataVO = new ChartDataVO();
            listHourDataVO.setItemValue(Double.valueOf(s.getParamValue()));
            listHourDataVO.setItemLable(s.getParamName());
            return listHourDataVO;
        }).collect(Collectors.toList());


        chartList.add(this.getChartVO2(new ChartInfoDTO()
                        .setChartName("直流电流监测")
                        .setChartUnit("A")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("直流电流")
                                .setItemStyle("0")
                                .setItemUnit("A")
                                .setDataList(list)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("总有功功率趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("kw")
                                .setParamId(130001L)
                                .setProjectType(ProjectTypeEnum.CDXM)
                        )
                )
        );

        List<ChartDataVO> list2 = sysDeviceMapper.listRealTimeData(new ChartDataDTO()
                .setDeviceId(9L)
                .setParamIdList(Arrays.asList(130011L,130013L,130015L,130017L))
                .setProjectTypeId(ProjectTypeEnum.CDXM.getId())
        ).stream().map(s->{
            ChartDataVO listHourDataVO = new ChartDataVO();
            listHourDataVO.setItemValue(Double.valueOf(s.getParamValue()));
            listHourDataVO.setItemLable(s.getParamName());
            return listHourDataVO;
        }).collect(Collectors.toList());

        chartList.add(this.getChartVO2(new ChartInfoDTO()
                        .setChartName("直流电压监测")
                        .setChartUnit("V")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("直流电压")
                                .setItemStyle("0")
                                .setItemUnit("V")
                                .setDataList(list2)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("总无功功率趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总无功功率")
                                .setItemStyle("0")
                                .setItemUnit("kvar")
                                .setParamId(130033L)
                                .setProjectType(ProjectTypeEnum.CDXM)
                        )
                )
        );
        return chartList;
    }

    @Override
    public WindGlobalViewVO windGlobalView(WindGlobalViewDTO dto) {
        WindGlobalViewVO windGlobalViewVO = new WindGlobalViewVO();
        windGlobalViewVO.setDataView(this.ruleParam(
                Collections.singletonList(100009L),
                Collections.singletonList("总装机容量"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        ).setLabel("数据一览"));
        windGlobalViewVO.setFdData(this.ruleParam(
                Arrays.asList(110004L,110003L,110041L),
                Arrays.asList("累计发电量","今日发电量","本月发电量"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        ));
        windGlobalViewVO.setSyData(this.ruleParam(
                Arrays.asList(100005L,100002L,100003L),
                Arrays.asList("累计收益","今日收益","本月收益"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        ));
        windGlobalViewVO.setSocialContribution(dataMapService.socialBenefit(
                Double.valueOf(windGlobalViewVO.getFdData().getChilds().get(0).getValue())));

        windGlobalViewVO.setFanMonitor(this.ruleParam(
                Arrays.asList(140007L,140006L,140004L,140005L),
                Arrays.asList("风机台数","并网台数","故障台数","通讯中断"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        ).setLabel("风机状态"));

        ItemVO itemVO = this.ruleParam(
                Arrays.asList(140002L, 140001L, 110021L),
                Arrays.asList("当前风向", "当前风速", "有功功率"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        );
        itemVO.getChilds().get(0).setValue("东南");
        windGlobalViewVO.setCurrentData(itemVO);

        windGlobalViewVO.setDevOpsData(this.ruleParam(
                Arrays.asList(100017L, 100018L, 100020L, 100023L),
                Arrays.asList("安全生产", "安全发电", "设备完好率", "今日告警数量"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        ).setLabel("运维数据"));

        windGlobalViewVO.setChartList(getChartVOList6());
        return windGlobalViewVO;
    }

    @NotNull
    private List<ChartVO> getChartVOList6() {
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("日发电量趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setIfIncrement(true)
                                .setItemName("日发电量")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.FDXM)
                        )
                )
        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("总有功功率趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("kw")
                                .setParamId(110021L)
                                .setProjectType(ProjectTypeEnum.FDXM)
                        )
                )
        );
        return chartList;
    }

    @Override
    public BlowerMonitorVO blowerMonitor(BlowerMonitorDTO dto) {
        BlowerMonitorVO blowerMonitorVO = new BlowerMonitorVO();
        blowerMonitorVO.setTopInfo(this.ruleParam(
                Arrays.asList(
                        140001L,110021L,110022L,
                        110003L,110041L,100001L
                ),
                Arrays.asList(
                        "平均风速","总有功功率","总无功功率",
                        "日发电量","月发电量","年发电量"
                ),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        ).setLabel("实时数据"));

        //风机数据模拟
        SysParamSimulate fdSimulate = sysParamSimulateMapper.selectById(103L);
        SysParamSimulate fsSimulate = sysParamSimulateMapper.selectById(104L);

        List<ItemVO> blowerInfo = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ItemVO item = new ItemVO();

            List<ItemVO> childs = new ArrayList<>();

            ItemVO itemVO = new ItemVO();
            itemVO.setLabel("状态");
            itemVO.setValue(i==7 ? "故障":"正常发电");
            childs.add(itemVO);

            ItemVO itemVO1 = new ItemVO();
            itemVO1.setLabel("编号");
            itemVO1.setValue((i+1)+"#");
            childs.add(itemVO1);

            ItemVO itemVO2 = new ItemVO();
            itemVO2.setLabel("发电量");
            itemVO2.setValue(String.valueOf(
                    CustomUtils.getRandomDouble(
                            fdSimulate.getParamLowerLimit(),
                            fdSimulate.getParamUpperLimit(),
                            fdSimulate.getParamBaseValue()
                    )
            ));
            itemVO2.setUnit("kWh");
            childs.add(itemVO2);

            ItemVO itemVO3 = new ItemVO();
            itemVO3.setLabel("风速");
            itemVO3.setValue(String.valueOf(
                    CustomUtils.getRandomDouble(
                            fsSimulate.getParamLowerLimit(),
                            fsSimulate.getParamUpperLimit(),
                            fsSimulate.getParamBaseValue()
                    )
            ));
            itemVO3.setUnit("m/s");
            childs.add(itemVO3);

            item.setChilds(childs);

            blowerInfo.add(item);
        }
        blowerMonitorVO.setBlowerInfo(new ItemVO().setChilds(blowerInfo));
        return blowerMonitorVO;
    }

    @Override
    public WaterGlobalViewVO waterGlobalView(WaterGlobalViewDTO dto) {
        WaterGlobalViewVO waterGlobalViewVO = new WaterGlobalViewVO();
        waterGlobalViewVO.setDataView(this.ruleParam(
                Arrays.asList(
                        110004L,110003L,110041L,100005L,
                        100002L,100003L,100009L,100008L
                ),
                Arrays.asList(
                        "累计发电量","今日发电量","本月发电量","累计收益",
                        "今日收益","本月收益","总装机容量","累计上网"
                ),
                Collections.singletonList(ProjectTypeEnum.SDXM.getId())
        ).setLabel("数据一览"));
        waterGlobalViewVO.setRealTime(this.ruleParam(
                Arrays.asList(160001L,160002L),
                Arrays.asList("水位高度","实时负荷"),
                Collections.singletonList(ProjectTypeEnum.SDXM.getId())
        ));
        waterGlobalViewVO.setSocialContribution(dataMapService.socialBenefit(
                Double.valueOf(waterGlobalViewVO.getDataView().getChilds().get(0).getValue())
        ).setLabel("社会贡献"));

        TideData data = tideUtil.getData();
        waterGlobalViewVO.setTimeAndLevel(getRealTimeData(data.getTimeAndLevel(),"潮时","潮高cm"));
        waterGlobalViewVO.setVelocityOfFlow(getRealTimeData(data.getVelocityOfFlow(),"流转时分","最大流流速"));

        waterGlobalViewVO.setChartList(getChartVOList7(data));
        return waterGlobalViewVO;
    }

    /**
     * list
     * @return
     */
    private List<List<DeviceRealTimeVO>> getRealTimeData(List<TimeAndLevel> list,String headName,String dataName){
        List<List<DeviceRealTimeVO>> resultList = new ArrayList<>();
        List<DeviceRealTimeVO> headlist = new ArrayList<>();
        List<DeviceRealTimeVO> datalist = new ArrayList<>();

        int count = 0;
        for (TimeAndLevel timeAndLevel : list) {
            if(count == 0){
                headlist.add(new DeviceRealTimeVO().setValue(headName).setStatus("0"));
                datalist.add(new DeviceRealTimeVO().setValue(dataName).setStatus("0"));
            }

            headlist.add(new DeviceRealTimeVO().setValue(timeAndLevel.getTime()).setStatus("0"));
            datalist.add(new DeviceRealTimeVO().setValue(String.valueOf(timeAndLevel.getValue())).setStatus("0"));

            count ++;
        }

        resultList.add(headlist);
        resultList.add(datalist);
        return resultList;
    }


    @NotNull
    private List<ChartVO> getChartVOList7(TideData data) {
        List<ChartVO> chartList = new ArrayList<>();

        chartList.add(this.getChartVO2(new ChartInfoDTO()
                        .setChartName("当日潮流预报-流速")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("流速")
                                .setItemStyle("0")
                                .setItemUnit("cm/s")
                                .setDataList(
                                        data.getDayVelocityOfFlow().stream().map(s -> {
                                            ChartDataVO listHourDataVO = new ChartDataVO();
                                            listHourDataVO.setItemValue(s.getValue());
                                            listHourDataVO.setItemLable(s.getTime());
                                            return listHourDataVO;
                                        }).collect(Collectors.toList())
                                )
                        )
                )
        );
        chartList.add(this.getChartVO2(new ChartInfoDTO()
                        .setChartName("当日潮汐预报-潮位")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("潮位")
                                .setItemStyle("0")
                                .setItemUnit("cm")
                                .setDataList(
                                        data.getDayTimeAndLevel().stream().map(s -> {
                                            ChartDataVO listHourDataVO = new ChartDataVO();
                                            listHourDataVO.setItemValue(s.getValue());
                                            listHourDataVO.setItemLable(s.getTime());
                                            return listHourDataVO;
                                        }).collect(Collectors.toList())
                                )
                        )
                )
        );
        return chartList;
    }

    @Override
    public BurstingGlobalViewVO burstingGlobalView(BurstingGlobalViewDTO dto) {
        BurstingGlobalViewVO burstingGlobalViewVO = new BurstingGlobalViewVO();

        burstingGlobalViewVO.setDeviceList(this.listDevice(new PvGlobalOverviewDTO().setProjectType(ProjectTypeEnum.JZSGF)));

        burstingGlobalViewVO.setFdData(this.ruleParam(
                Arrays.asList(110004L,110003L,110041L),
                Arrays.asList("累计发电量","今日发电量","本月发电量"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId()),
                Collections.singletonList(12L)
        ).setLabel("数据一览"));

        ItemVO tempItem1 = burstingGlobalViewVO.getFdData().getChilds().get(0);
        ItemVO tempItem2 = burstingGlobalViewVO.getFdData().getChilds().get(1);
        ItemVO tempItem3 = burstingGlobalViewVO.getFdData().getChilds().get(2);

        tempItem1.setUnit("万kWh").setValue(new BigDecimal(tempItem1.getValue()).add(BigDecimal.valueOf(CommonConstants.totalFactor)).toPlainString());
        tempItem2.setUnit("万kWh").setValue(new BigDecimal(tempItem2.getValue()).add(BigDecimal.valueOf(CommonConstants.todayFactor)).toPlainString());
        tempItem3.setUnit("万kWh").setValue(new BigDecimal(tempItem3.getValue()).add(BigDecimal.valueOf(CommonConstants.monthFactor)).toPlainString());



//        burstingGlobalViewVO.setSyData(this.ruleParam(
//                Arrays.asList(100005L,100002L,100003L),
//                Arrays.asList("累计收益","今日收益","本月收益"),
//                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
//        ));

        burstingGlobalViewVO.setSyData(new ItemVO().setChilds(
                Arrays.asList(
                        new ItemVO().setValue(new BigDecimal(tempItem1.getValue()).multiply(BigDecimal.valueOf(0.3545)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()).setUnit("万元").setLabel("累计收益"),
                        new ItemVO().setValue(new BigDecimal(tempItem2.getValue()).multiply(BigDecimal.valueOf(0.3545)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()).setUnit("万元").setLabel("今日收益"),
                        new ItemVO().setValue(new BigDecimal(tempItem3.getValue()).multiply(BigDecimal.valueOf(0.3545)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()).setUnit("万元").setLabel("本月收益")
                )
        ));


        burstingGlobalViewVO.setOtherData(this.ruleParam(
                Arrays.asList(100009L,100008L),
                Arrays.asList("总装机容量","总上网"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
        ));
        burstingGlobalViewVO.getOtherData().getChilds().get(0).setUnit("MW");
        burstingGlobalViewVO.getOtherData().getChilds().get(1).setUnit("万kW").setLabel("集控接入容量").setValue("10");

        burstingGlobalViewVO.setEnvData(this.ruleParam(
                Arrays.asList(170005L,170006L,170001L,170008L,170009L),
                Arrays.asList("湿度","气压","环境温度","风速","风向"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId()),
                Collections.singletonList(12L)
        ).setLabel("今日环境数据"));

        //burstingGlobalViewVO.getEnvData().getChilds().remove(0);

        burstingGlobalViewVO.setInverterData(this.ruleParam(
                Arrays.asList(
                        150001L,150002L,
                        150010L,150003L,
                        150004L,150005L,
                        150006L,150007L
                ),
                Arrays.asList(
                        "A相并网电流","B相并网电流",
                        "C相并网电流","电网AB线电压",
                        "电网BC线电压","电网CA线电压",
                        "直流母线电流","直流母线电压"
                ),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
        ).setLabel("逆变器监测"));

        burstingGlobalViewVO.setFailureData(
                this.ruleParam(
                        Arrays.asList(100023L,150008L),
                        Arrays.asList("今日告警数量","故障停机"),
                        Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
                )
        );

        burstingGlobalViewVO.setDevOpsData(this.ruleParam(
                Arrays.asList(100017L, 100018L, 100020L),
                Arrays.asList("安全生产", "安全发电", "设备完好率"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
        ));

        burstingGlobalViewVO.getDevOpsData().getChilds().get(1).setValue(new BigDecimal(tempItem1.getValue()).multiply(BigDecimal.valueOf(10)).divide(new BigDecimal(burstingGlobalViewVO.getOtherData().getChilds().get(0).getValue()),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());


        burstingGlobalViewVO.setSocialBenefit(dataMapService.socialBenefit(
                Double.valueOf(burstingGlobalViewVO.getFdData().getChilds().get(0).getValue())
        ));
        burstingGlobalViewVO.getSocialBenefit().getChilds().get(0).setUnit("万吨");
        burstingGlobalViewVO.getSocialBenefit().getChilds().get(1).setUnit("万吨");

//        String value = burstingGlobalViewVO.getFdData().getChilds().get(0).getValue();
//        burstingGlobalViewVO.setSocialBenefit(new ItemVO().setLabel("社会效益").setChilds(Arrays.asList(
//              new ItemVO().setLabel("CO2减排").setUnit("万吨").setValue(new BigDecimal(value).multiply(BigDecimal.valueOf(0.997)).divide(BigDecimal.valueOf(1000),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()),
//              new ItemVO().setLabel("标煤节约").setUnit("万吨").setValue(new BigDecimal(value).multiply(BigDecimal.valueOf(0.36)).divide(BigDecimal.valueOf(1000),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
//        )));



        burstingGlobalViewVO.setChartList(getChartVOList8());

        return burstingGlobalViewVO;
    }

    @Override
    public BpvInverterMonitoringVO bpvInverterMonitoring(BpvInverterMonitoringDTO dto) {
        BpvInverterMonitoringVO inverterMonitoringVO = new BpvInverterMonitoringVO();
        inverterMonitoringVO.setInverterDataStatistics(this.ruleParam(
                Collections.singletonList(110004L),
                Collections.singletonList("累计发电量"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId()),
                Collections.singletonList(12L)
        ).setLabel("逆变器数据统计"));

        ItemVO itemVO = inverterMonitoringVO.getInverterDataStatistics().getChilds().get(0);
        itemVO.setUnit("万kWh").setValue(new BigDecimal(itemVO.getValue()).add(BigDecimal.valueOf(CommonConstants.totalFactor)).stripTrailingZeros().toPlainString());

        inverterMonitoringVO.setOtherStatistics(this.ruleParam(
                Arrays.asList(110003L,110021L, 110041L,110022L),
                Arrays.asList("当日总发电量","总有功功率","当月总发电量","总无功功率"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId()),
                Collections.singletonList(12L)
        ));
        ItemVO itemVO1 = inverterMonitoringVO.getOtherStatistics().getChilds().get(0);
        ItemVO itemVO2 = inverterMonitoringVO.getOtherStatistics().getChilds().get(1);
        ItemVO itemVO3 = inverterMonitoringVO.getOtherStatistics().getChilds().get(2);

        itemVO1.setUnit("万kWh").setValue(new BigDecimal(itemVO1.getValue()).add(BigDecimal.valueOf(CommonConstants.todayFactor)).stripTrailingZeros().toPlainString());
        itemVO2.setUnit("MW").setValue(new BigDecimal(itemVO2.getValue()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        itemVO3.setUnit("万kWh").setValue(new BigDecimal(itemVO3.getValue()).add(BigDecimal.valueOf(CommonConstants.monthFactor)).stripTrailingZeros().toPlainString());


        inverterMonitoringVO.setChartList(getChartVOList2_1());

        inverterMonitoringVO.setRealTimeData(this.realTimeData(
                new ChartDataDTO()
                        .setProjectTypeId(ProjectTypeEnum.JZSGF.getId())
                        .setExcludeCustomParam(true)
                        .setIfBpv(true)
                        .setParamIdList(Arrays.asList(
                                110093L,150003L,150004L,150005L,
                                150001L,150010L,150002L,110023L,
                                110024L,110021L,110022L,110007L,
                                110006L,110004L,110003L,110041L
                        )).setSubmatrix(dto.getSubmatrix())
        ));

//        List<DeviceRealTimeVO> deviceRealTimeVOS = inverterMonitoringVO.getRealTimeData().get(1);
//        for (int i = 0; i < deviceRealTimeVOS.size(); i++) {
//            if(i != 0){
//                deviceRealTimeVOS.get(i).setValue("正常").setStatus("0");
//            }
//        }
        if(CollectionUtil.isNotEmpty(inverterMonitoringVO.getRealTimeData()) && inverterMonitoringVO.getRealTimeData().size()>=2){
            inverterMonitoringVO.getRealTimeData().remove(1);
        }

        return inverterMonitoringVO;
    }

    @NotNull
    private List<ChartVO> getChartVOList8() {
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName("光电数据实时监测")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("MW")
                                .setParamId(110021L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                                .setDeviceIdList(Collections.singletonList(12L))
                        )
                )
        );


        List<ChartDataVO> list = dataParamConfigMapper.getConfigChartData(
                new GetConfigChartDataDTO().setChartId(ChartTypeEnum.FSD.getId())
                        .setProjectId(ProjectTypeEnum.JZSGF.getId())
        );
        chartList.add(this.getChartVO2(new ChartInfoDTO()
                        .setChartName(CTypeUtils.ifMobile() ? "辐射度趋势":"")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("辐射度")
                                .setItemStyle("0")
                                .setItemUnit("w/m2")
                                .setDataList(list)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setChartName(CTypeUtils.ifMobile() ? "日发电量趋势":"")
                        .setChartItem(new ChartItemInfoDTO()
                                //.setIfIncrement(true)
                                .setItemName("日发电量")
                                .setItemStyle("0")
                                .setItemUnit("万kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                                .setDeviceIdList(Collections.singletonList(12L))
                        )
                )
        );
        return chartList;
    }

    @Override
    public BurstingDataStatisticsVO burstingDataStatistics(BurstingDataStatisticsDTO dto) {
        BurstingDataStatisticsVO burstingDataStatisticsVO = new BurstingDataStatisticsVO();
        burstingDataStatisticsVO.setTopInfo(this.ruleParam(
                Arrays.asList(110004L,100005L,100009L,100008L,110021L),
                Arrays.asList("累计发电量","累计收益","总装机容量","累计上网","总有功功率"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
        ));
        burstingDataStatisticsVO.setChartList(getChartVOList9(dto));
        return burstingDataStatisticsVO;
    }

    @Override
    public List<String> listSubmatrix() {
        List<SysDevice> sysDeviceList = sysDeviceMapper.selectList(new LambdaQueryWrapper<SysDevice>()
                .eq(SysDevice::getProjectId, 2)
        );
        return sysDeviceList.stream().map(SysDevice::getRemarks).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
    }

    @NotNull
    private List<ChartVO> getChartVOList9(BurstingDataStatisticsDTO dto) {
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("发电量及增长情况")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("发电量")
                                .setItemStyle("0")
                                .setItemUnit("万kWh")
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("同比增长")
                                .setItemStyle("0")
                                .setItemUnit("%")
                                .setParamId(110041L)
                                .setIfYoy(true)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                        )
                )
        );
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("总有功功率趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("总有功功率")
                                .setItemStyle("0")
                                .setItemUnit("kw")
                                .setParamId(110021L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                        )
                )
        );

        //辐照度及发电量预测 模拟数据
        SysParamSimulate fdycSimulate = sysParamSimulateMapper.selectById(105L);
        SysParamSimulate fzycSimulate = sysParamSimulateMapper.selectById(106L);

        chartList.add(new ChartVO()
                .setLabel("辐照度及发电量预测")
                .setChilds(Arrays.asList(
                        prediction(new PredictionDTO()
                                .setItemName("辐照度")
                                .setItemStyle("0")
                                .setItemUnit("w/m2")
                                .setParamLowerLimit(fzycSimulate.getParamLowerLimit())
                                .setParamUpperLimit(fzycSimulate.getParamUpperLimit())
                                .setParamBaseValue(fzycSimulate.getParamBaseValue())
                                .setType(dto.getType())
                        ),
                        prediction(new PredictionDTO()
                                .setItemName("发电量")
                                .setItemStyle("0")
                                .setItemUnit("kwh")
                                .setParamLowerLimit(fdycSimulate.getParamLowerLimit())
                                .setParamUpperLimit(fdycSimulate.getParamUpperLimit())
                                .setParamBaseValue(fdycSimulate.getParamBaseValue())
                                .setType(dto.getType())
                        )
                ))
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("收益分析")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("收益")
                                .setItemStyle("0")
                                .setItemUnit("万元")
                                .setParamId(100003L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                        )
                )
        );
        return chartList;
    }

    /**
     * 逆变器实时数据
     * @param dto
     * @return
     */
    public List<List<DeviceRealTimeVO>> realTimeData(ChartDataDTO dto){
        List<ListRealTimeDataVO> list = sysDeviceMapper.listRealTimeData(dto);
        if(CollectionUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        Map<Long, List<ListRealTimeDataVO>> map = list.stream().collect(Collectors.groupingBy(ListRealTimeDataVO::getParamId));

        Map<Long,String> tempMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(dto.getParamNameList())){
            for (int i = 0; i < dto.getParamNameList().size(); i++) {
                tempMap.put(dto.getParamIdList().get(i),dto.getParamNameList().get(i));
            }
        }else {
            Set<Map.Entry<Long, List<ListRealTimeDataVO>>> tempEntries = map.entrySet();
            for (Map.Entry<Long, List<ListRealTimeDataVO>> tempEntrie : tempEntries) {
                ListRealTimeDataVO vo = tempEntrie.getValue().get(0);
                tempMap.put(tempEntrie.getKey(), vo.getParamName() +(StrUtil.isNotBlank(vo.getParamUnit()) ? "("+vo.getParamUnit()+")":"")
                );
            }
        }

        int count = 0;
        List<List<DeviceRealTimeVO>> resultList = new ArrayList<>();

        for (Long paramId : dto.getParamIdList()) {
            List<DeviceRealTimeVO> headList = new ArrayList<>();
            List<DeviceRealTimeVO> deviceList = new ArrayList<>();

            if(count == 0){
                headList.add(new DeviceRealTimeVO()
                        .setValue("")
                        .setStatus("0")
                );
            }

            deviceList.add(new DeviceRealTimeVO()
                    .setValue(tempMap.get(paramId))
                    .setStatus("0")
            );

            List<ListRealTimeDataVO> collect;
            if(dto.isIfBpv()){
                collect = map.get(paramId).stream().sorted(Comparator.comparing(ListRealTimeDataVO::getDeviceId, Comparator.naturalOrder())).collect(Collectors.toList());
            }else {
                collect = map.get(paramId).stream().sorted(Comparator.comparing(ListRealTimeDataVO::getPlace, Comparator.naturalOrder())).collect(Collectors.toList());
            }

            for (ListRealTimeDataVO vo : collect) {
                if(count == 0){
                    headList.add(new DeviceRealTimeVO()
                            .setValue(dto.isIfBpv() ? vo.getDeviceName() : vo.getDeviceRemark() + vo.getDeviceName())
                            .setStatus("0")
                    );
                }

                DeviceRealTimeVO deviceRealTimeVO = new DeviceRealTimeVO()
                        .setValue(String.valueOf(vo.getParamValue()))
                        .setStatus("0");

                if(vo.getStatusParamValue()!=-1){
                    deviceRealTimeVO.setValue(vo.getStatusParamValue()==1 ? "正常":"异常");
                    deviceRealTimeVO.setStatus(vo.getStatusParamValue()==1 ? "0":"1");
                }

                deviceList.add(deviceRealTimeVO);

            }

            if(CollectionUtil.isNotEmpty(headList)){
                resultList.add(headList);
            }
            resultList.add(deviceList);

            count++;
        }

        return resultList;
    }
    

    /**
     * 自选参数查询
     * @param paramIdList
     * @param projectTypeIdList
     * @return
     */
    public ItemVO ruleParam(List<Long> paramIdList,List<Long> projectTypeIdList){
        return ruleParam(paramIdList,null,projectTypeIdList);
    }


    public ItemVO ruleParam(List<Long> paramIdList,List<String> paramNameList,List<Long> projectTypeIdList,List<Long> deviceIdList){
        return ruleParam(paramIdList, paramNameList, projectTypeIdList,null,deviceIdList);
    }

    /**
     * 自选参数查询 - 自定义参数名
     * @param paramIdList
     * @param projectTypeIdList
     * @return
     */
    public ItemVO ruleParam(List<Long> paramIdList,List<String> paramNameList,List<Long> projectTypeIdList){
        return ruleParam(paramIdList, paramNameList, projectTypeIdList,null,null);
    }

    /**
     * 自选参数查询 - 自定义参数名
     * @param paramIdList
     * @param paramNameList
     * @param projectTypeIdList
     * @param projectIds
     * @return
     */
    public ItemVO ruleParam(List<Long> paramIdList, List<String> paramNameList, List<Long> projectTypeIdList,String projectIds,List<Long> deviceIdList) {
        List<RuleParamVO> list = sysDeviceMapper.ruleParam(new ListDeviceDTO()
                .setProjectTypeIdList(projectTypeIdList)
                .setParamIdList(paramIdList)
                .setParamIds(StrUtil.join(",", paramIdList))
                .setProjectIds(projectIds)
                .setDeviceIdList(deviceIdList)
        );

        AtomicInteger count = new AtomicInteger();

        ItemVO itemVO = new ItemVO();
        List<ItemVO> childs = list.stream().map(temp -> {

            ItemVO child = new ItemVO();
            child.setLabel(CollectionUtil.isNotEmpty(paramNameList) ? paramNameList.get(count.get()) : temp.getParamName());
            if(temp.getStatusParamValue() != -1){
                child.setValue(temp.getStatusParamValue() == 1 ? "正常":"异常");
            }else if(temp.getParamId().equals(100020L)){
                child.setValue(BigDecimal.valueOf(temp.getParamValue()).multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString() +"%");
            }else if(temp.getParamId().equals(100017L) && temp.isIfConfig()){
                child.setValue(BigDecimal.valueOf(temp.getParamValue()).add(BigDecimal.valueOf(DateUtil.between(DateUtil.parse("2024-01-31","yyyy-MM-dd"),new Date(), DateUnit.DAY))).toPlainString());
            }else if(temp.getParamId().equals(100018L) && temp.isIfConfig()){
                child.setValue(BigDecimal.valueOf(temp.getParamValue()).add((BigDecimal.valueOf(DateUtil.between(DateUtil.parse("2024-01-31","yyyy-MM-dd"),new Date(), DateUnit.DAY))).multiply(BigDecimal.valueOf(24))).toPlainString());
            }else if(temp.getParamId().equals(100023L) && temp.isIfConfig()){
                child.setValue(DateUtil.compare(temp.getParamTime(),DateUtil.beginOfDay(new Date()))>=0 ? temp.getParamValue().toString() : "0");
            }else if(temp.getParamId().equals(120072L)){
                child.setValue(BigDecimal.valueOf(temp.getParamValue()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()+" %");
            }else if(temp.getParamId().equals(120073L)){
                child.setValue(BigDecimal.valueOf(temp.getParamValue()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()+" %");
            }else {
                child.setValue(String.valueOf(temp.getParamValue()));
            }

            child.setUnit(temp.getParamUnit());
            count.getAndIncrement();
            return child;
        }).collect(Collectors.toList());

        return itemVO.setChilds(childs);
    }

    @Override
    public MoBileHomePageVO moBileHomePage(MapDataCountDTO dto) {
        //项目权限
        List<Long> projectIdList = sysUserMenuMapper.getProjectIdListHavePerms(StpUtil.getLoginIdAsLong());
        dto.setProjectIds(CollectionUtil.isEmpty(projectIdList) ? "" : StrUtil.join(",",projectIdList));

        MoBileHomePageVO moBileHomePageVO = new MoBileHomePageVO();

        dto.setProjectTypeIdList(Arrays.asList(ProjectTypeEnum.JZSGF.getId(),ProjectTypeEnum.FBSGF.getId()));
        ItemVO powerItemVO = dataMapService.otherCount(dto, GeneralParamEnum.ZFDL);
        BigDecimal jzsDecimal = BigDecimal.valueOf(Double.parseDouble(powerItemVO.getChilds().get(0).getValue()));
        BigDecimal fbsDecimal = BigDecimal.valueOf(Double.parseDouble(powerItemVO.getChilds().get(1).getValue()));
        BigDecimal divide = jzsDecimal.divide(jzsDecimal.add(fbsDecimal), 2, RoundingMode.HALF_UP);
        BigDecimal subtract = BigDecimal.valueOf(1).subtract(divide);
        moBileHomePageVO.setPowerScale(new ChartVO().setLabel("发电量占比分析")
                .setChilds(Arrays.asList(
                        new ChartVO().setLabel("集中式光伏").setValue(divide.doubleValue()),
                        new ChartVO().setLabel("分布式光伏").setValue(subtract.doubleValue())
                ))
        );

//        dto.setProjectTypeIdList(Arrays.asList(ProjectTypeEnum.JZSGF.getId(),ProjectTypeEnum.FBSGF.getId(),ProjectTypeEnum.FDXM.getId()));
//        ItemVO todayPowerItemVO = dataMapService.otherCount(dto, GeneralParamEnum.JRZFDL);
//        ItemVO monthPowerItemVO = dataMapService.otherCount(dto, GeneralParamEnum.BYZFDL);
//        ItemVO totalPowerItemVO = dataMapService.otherCount(dto, GeneralParamEnum.ZFDL);

        //发电量
        ItemVO data1 = dataMapService.otherCount(dto, GeneralParamEnum.JRZFDL);
        ItemVO data2 = dataMapService.otherCount(dto, GeneralParamEnum.BYZFDL);
        ItemVO data3 = dataMapService.otherCount(dto, GeneralParamEnum.ZFDL);
        ItemVO data11 = data1.getChilds().get(0);
        ItemVO data12 = data1.getChilds().get(1);
        ItemVO data21 = data2.getChilds().get(0);
        ItemVO data22 = data2.getChilds().get(1);
        ItemVO data31 = data3.getChilds().get(0);
        ItemVO data32 = data3.getChilds().get(1);
        ItemVO itemVO3 = new ItemVO().setChilds(Arrays.asList(
                new ItemVO().setLabel("日发电量").setUnit("万kWh").setValue(new BigDecimal(data11.getValue()).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(data12.getValue())).add(BigDecimal.valueOf(CommonConstants.todayFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()),
                new ItemVO().setLabel("月发电量").setUnit("万kWh").setValue(new BigDecimal(data21.getValue()).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(data22.getValue())).add(BigDecimal.valueOf(CommonConstants.monthFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()),
                new ItemVO().setLabel("总发电量").setUnit("万kWh").setValue(new BigDecimal(data31.getValue()).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(data32.getValue())).add(BigDecimal.valueOf(CommonConstants.totalFactor)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
        ));
        moBileHomePageVO.setPowerCount(itemVO3);


        //项目数
        ItemVO countItemVO = dataMapService.totalItems(dto);

        //总装机数据
        ItemVO zzjrlItemVO = dataMapService.otherCount(dto,GeneralParamEnum.ZZJRL);
        ItemVO tempItem1 = zzjrlItemVO.getChilds().get(0);
        tempItem1.setValue(new BigDecimal(tempItem1.getValue()).divide(BigDecimal.valueOf(1000),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        zzjrlItemVO.setValue(new BigDecimal(tempItem1.getValue()).add(new BigDecimal(zzjrlItemVO.getChilds().get(1).getValue())).stripTrailingZeros().toPlainString());


        //总收益
        ItemVO zsyItemVO = dataMapService.otherCount(dto,GeneralParamEnum.ZSY);
        ItemVO tempItem2 = zsyItemVO.getChilds().get(1);
        tempItem2.setValue(new BigDecimal(data32.getValue()).add(BigDecimal.valueOf(CommonConstants.totalFactor)).multiply(BigDecimal.valueOf(0.3545)).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        zsyItemVO.setValue(new BigDecimal(tempItem2.getValue()).add(new BigDecimal(zsyItemVO.getChilds().get(0).getValue())).stripTrailingZeros().toPlainString());


        //运维数据
        ItemVO itemVO1 = this.ruleParam(Arrays.asList(100017L, 100018L), Arrays.asList("安全生产","安全发电"),
                Collections.singletonList(ProjectTypeEnum.FBSGF.getId())
        );
        //首页 -> 安全生产:有配置取配置的最大值; 安全发电:各项目安全发电总和;
        extracted(itemVO1);
        //安全发电
        itemVO1.getChilds().get(1).setValue(new BigDecimal(itemVO3.getChilds().get(2).getValue()).multiply(BigDecimal.valueOf(10)).divide(new BigDecimal(zzjrlItemVO.getValue()),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());


        moBileHomePageVO.setDevOpsData(new ItemVO().setChilds(
                Arrays.asList(
                        new ItemVO().setUnit("").setLabel("项目总数量").setValue(countItemVO.getValue()),
                        new ItemVO().setUnit("MW").setLabel("总装机容量").setValue(zzjrlItemVO.getValue()),
                        new ItemVO().setUnit("天").setLabel("安全生产").setValue(itemVO1.getChilds().get(0).getValue()),
                        new ItemVO().setUnit("万元").setLabel("总收益").setValue(zsyItemVO.getValue()),
                        new ItemVO().setUnit("小时").setLabel("安全发电").setValue(itemVO1.getChilds().get(1).getValue())
                )
        ));

        moBileHomePageVO.setSocialBenefit(dataMapService.socialBenefit(Double.valueOf(itemVO3.getChilds().get(2).getValue())));
        moBileHomePageVO.getSocialBenefit().getChilds().get(0).setUnit("万吨");
        moBileHomePageVO.getSocialBenefit().getChilds().get(1).setUnit("万吨");

        moBileHomePageVO.setChartList(getChartVOList10());

        return moBileHomePageVO;
    }

    private void extracted(ItemVO itemVO1) {
        ItemVO itemVO2 = this.ruleParam(Arrays.asList(100017L, 100018L), Arrays.asList("安全生产", "安全发电"),
                Collections.singletonList(ProjectTypeEnum.JZSGF.getId())
        );
        ItemVO itemVO3 = this.ruleParam(Arrays.asList(100017L, 100018L), Arrays.asList("安全生产", "安全发电"),
                Collections.singletonList(ProjectTypeEnum.FDXM.getId())
        );
        ItemVO itemVO4 = this.ruleParam(Arrays.asList(100017L, 100018L), Arrays.asList("安全生产", "安全发电"),
                Collections.singletonList(ProjectTypeEnum.CNXM.getId())
        );
        ItemVO itemVO5 = this.ruleParam(Arrays.asList(100017L, 100018L), Arrays.asList("安全生产", "安全发电"),
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
    }


    private List<ChartVO> getChartVOList10(){
        List<ChartVO> chartList = new ArrayList<>();
        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("hour")
                        .setChartName("今日发电量趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("集中式光伏")
                                .setItemStyle("0")
                                .setItemUnit("万kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                                .setDeviceIdList(Collections.singletonList(12L))
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("分布式光伏")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("bmonth")
                        .setChartName("本月发电量趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("集中式光伏")
                                .setItemStyle("0")
                                .setItemUnit("万kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                                .setDeviceIdList(Collections.singletonList(12L))
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("分布式光伏")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(110003L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );

        chartList.add(this.getChartVO(new ChartInfoDTO()
                        .setDataType("month")
                        .setChartName("年度发电量趋势")
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("集中式光伏")
                                .setItemStyle("0")
                                .setItemUnit("万kWh")
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.JZSGF)
                                .setDeviceIdList(Collections.singletonList(12L))
                        )
                        .setChartItem(new ChartItemInfoDTO()
                                .setItemName("分布式光伏")
                                .setItemStyle("0")
                                .setItemUnit("kWh")
                                .setParamId(110041L)
                                .setProjectType(ProjectTypeEnum.FBSGF)
                        )
                )
        );
        return chartList;
    }



    /**
     * 环境数据
     * @return
     */
    private ItemVO envDataToday(Long projectId){
        String cityCode = weatherCityCodeXian;
        if(ProjectTypeEnum.JZSGF.getId().equals(projectId)){
            cityCode = weatherCityCodeYanan;
        }
        WeatherData data = weatherUtil.getData(null,cityCode);

        Map<Long, String> map = dataParamConfigMapper.listDataParamConfig(new DataCountDTO()
                .setProjectId(projectId)
                .setIdList(Arrays.asList(100108L, 100109L, 100110L, 100111L, 100112L, 100113L))
        ).stream().filter(temp->temp.getIfEnable()==1).collect(Collectors.toMap(DataCountVO::getId, DataCountVO::getParamValue));

        ItemVO itemVO = new ItemVO();
        List<ItemVO> childs = new ArrayList<>();
        if(CTypeUtils.ifPc() || CTypeUtils.ifMobile()){
            childs.add(new ItemVO().setLabel("辐射度").setValue(!map.containsKey(100108L)?data.getLightIntensity():map.get(100108L)).setIcon("1").setUnit("W/m2"));
        }else {
            childs.add(new ItemVO().setLabel("光照强度").setValue(!map.containsKey(100108L)?data.getLightIntensity():map.get(100108L)).setIcon("1").setUnit("lx"));
        }
        childs.add(new ItemVO().setLabel("湿度").setValue(!map.containsKey(100109L)?data.getHumidity():map.get(100109L)).setIcon("2").setUnit("%"));
        childs.add(new ItemVO().setLabel("气压").setValue(!map.containsKey(100110L)?data.getPressure():map.get(100110L)).setIcon("3").setUnit("hpa"));
        childs.add(new ItemVO().setLabel("环境温度").setValue(!map.containsKey(100111L)?data.getTemperature():map.get(100111L)).setIcon("4").setUnit("℃"));
        childs.add(new ItemVO().setLabel("风速").setValue(!map.containsKey(100112L)?data.getWindScale():map.get(100112L)).setIcon("5").setUnit("级"));
        childs.add(new ItemVO().setLabel("风向").setValue(!map.containsKey(100113L)?data.getWindDirection():map.get(100113L)).setIcon("5").setUnit(""));

        return itemVO.setChilds(childs).setLabel("今日环境数据");
    }

    /**
     * 设备列表
     * @param dto
     * @return
     */
    public List<ListDeviceVO> listDevice(PvGlobalOverviewDTO dto){
        List<ListDeviceVO> listDevice = sysDeviceMapper.listDeviceByPojectType(new ListDeviceDTO()
                .setProjectTypeId(dto.getProjectType().getId())
                .setParamId(GeneralParamEnum.ZFDL.getId())
        );

        SysParamSimulate sySimulate = sysParamSimulateMapper.selectById(102L);

        for (ListDeviceVO listDeviceVO : listDevice) {
            listDeviceVO.setOtherData(new ItemVO()
                    .setLabel("其他数据")
                    .setChilds(Arrays.asList(
                            new ItemVO().setLabel("总发电量").setValue(String.valueOf(listDeviceVO.getParamValue())).setUnit("kWh"),
                            new ItemVO().setLabel("总收益").setValue(String.valueOf(listDeviceVO.getParamValue() * sySimulate.getParamBaseValue())).setUnit("元")
                    ))
            );
            listDeviceVO.setSocialBenefit(dataMapService.socialBenefit(listDeviceVO.getParamValue()));
        }
        return listDevice;
    }

    public List<String> getHours() {
        List<String> list = new ArrayList<>();
        int hour = DateUtil.thisHour(true);
        for (int i = 0; i <= hour; i++) {
            if(i>=10){
                list.add(i+":00");
            }else {
                list.add("0"+i+":00");
            }
        }
        return list;
    }

    /**
     * 图表集合
     * @return
     */
    public ChartVO getChartVO(ChartInfoDTO dto){
        ChartVO chartVO = new ChartVO();
        chartVO.setLabel(dto.getChartName());
        chartVO.setUnit(dto.getChartUnit());

        List<ChartVO> items = new ArrayList<>();
        for (ChartItemInfoDTO chartItemDTO : dto.getChartItemList()) {
            List<ChartDataVO> list = new ArrayList<>();
            if(dto.getDataType().equals("hour")){
                list = sysDeviceMapper.listHourData(new ChartDataDTO()
                        .setProjectTypeId(chartItemDTO.getProjectType().getId())
                        .setParamId(chartItemDTO.getParamId())
                        .setParamTime(DateUtil.format(new Date(),"yyyy-MM-dd"))
                        .setDeviceIdList(chartItemDTO.getDeviceIdList())
                );

                List<ChartDataVO> listTemp = new ArrayList<>();
                Map<String, ChartDataVO> map = list.stream().collect(Collectors.toMap(ChartDataVO::getItemLable, t->t));
                List<String> hours = getHours();
                for (String hour : hours) {
                    if(map.containsKey(hour)){
                        listTemp.add(map.get(hour));
                    }else {
                        ChartDataVO chartDataVO = new ChartDataVO();
                        chartDataVO.setItemValue(0d);
                        chartDataVO.setItemLable(hour);
                        listTemp.add(chartDataVO);
                    }
                }
                list = listTemp;

            }else if(dto.getDataType().equals("bmonth")){
                list = sysDeviceMapper.listBenYueData(new ChartDataDTO()
                        .setProjectTypeId(chartItemDTO.getProjectType().getId())
                        .setParamId(chartItemDTO.getParamId())
                        .setParamTime(DateUtil.format(new Date(),"yyyy-MM"))
                        .setDeviceIdList(chartItemDTO.getDeviceIdList())
                );
            }else if(dto.getDataType().equals("month")){
                List<ChartDataVO> listHourDataVOS = sysDeviceMapper.listMonthData(new ChartDataDTO()
                        .setProjectTypeId(chartItemDTO.getProjectType().getId())
                        .setParamId(chartItemDTO.getParamId())
                        .setDeviceIdList(chartItemDTO.getDeviceIdList())
                );
                list = CollectionUtil.reverse(listHourDataVOS);
            }else if(dto.getDataType().equals("day")){
                List<ChartDataVO> listHourDayVOS = sysDeviceMapper.listDayData(new ChartDataDTO()
                        .setProjectTypeId(chartItemDTO.getProjectType().getId())
                        .setParamId(chartItemDTO.getParamId())
                        .setDeviceIdList(chartItemDTO.getDeviceIdList())
                );
                list = CollectionUtil.reverse(listHourDayVOS);
            }

            ChartVO item = new ChartVO();
            item.setLabel(chartItemDTO.getItemName());
            item.setStyle(chartItemDTO.getItemStyle());
            item.setUnit(chartItemDTO.getItemUnit());

            List<ChartVO> childs = new ArrayList<>();

            if(chartItemDTO.isIfIncrement()){
                for (int i = 0; i < list.size(); i++) {
                    ChartVO child = new ChartVO();
                    if(i==0){
                        child.setLabel(list.get(i).getItemLable());
                        child.setValue(0d);
                    }else {
                        child.setLabel(list.get(i).getItemLable());
                        child.setValue(Math.abs(list.get(i).getItemValue()-list.get(i-1).getItemValue()));
                    }
                    childs.add(child);
                }
            }
            else if(chartItemDTO.isIfYoy() && dto.getDataType().equals("month")){
                List<ChartDataVO> listHourDataVOS = sysDeviceMapper.listMonthData(new ChartDataDTO()
                        .setProjectTypeId(chartItemDTO.getProjectType().getId())
                        .setParamId(chartItemDTO.getParamId())
                        .setDeviceIdList(chartItemDTO.getDeviceIdList())
                        .setLimitStart(12)
                        .setLimitEnd(23)
                );
                List<ChartDataVO> reverse = CollectionUtil.reverse(listHourDataVOS);

                for (int i = 0; i < list.size() && i< reverse.size(); i++) {
                    ChartVO child = new ChartVO();
                    child.setLabel(list.get(i).getItemLable());
                    if(CollectionUtil.isEmpty(reverse) || BeanUtil.isEmpty(reverse.get(i))){
                        child.setValue(0d);
                    }else {
                        child.setValue((list.get(i).getItemValue()-reverse.get(i).getItemValue())/reverse.get(i).getItemValue());
                    }
                    childs.add(child);
                }
            }
            else {
                childs = list.stream().map(temp-> new ChartVO()
                        .setValue(temp.getItemValue())
                        .setLabel(temp.getItemLable())
                ).collect(Collectors.toList());
            }

            item.setChilds(childs);

            items.add(item);
        }

        return chartVO.setChilds(items);
    }

    /**
     * 图表集合
     * @return
     */
    public ChartVO getChartVO2(ChartInfoDTO dto){
        ChartVO chartVO = new ChartVO();
        chartVO.setLabel(dto.getChartName());
        chartVO.setUnit(dto.getChartUnit());

        List<ChartVO> items = new ArrayList<>();
        for (ChartItemInfoDTO chartItemDTO : dto.getChartItemList()) {
            ChartVO item = new ChartVO();
            item.setLabel(chartItemDTO.getItemName());
            item.setStyle(chartItemDTO.getItemStyle());
            item.setUnit(chartItemDTO.getItemUnit());

            item.setChilds(
                    chartItemDTO.getDataList().stream().map(temp-> new ChartVO()
                    .setValue(temp.getItemValue())
                    .setLabel(temp.getItemLable())
            ).collect(Collectors.toList()));

            items.add(item);
        }
        return chartVO.setChilds(items);
    }

    /**
     * 辐照度 和 发电量 预测
     * @return
     */
    private ChartVO prediction(PredictionDTO dto){
        ChartVO item = new ChartVO();
        item.setLabel(dto.getItemName());
        item.setStyle(dto.getItemStyle());
        item.setUnit(dto.getItemUnit());

        List<ChartVO> items = new ArrayList<>();

        if(dto.getType()==1){
            for (int i = 1; i <= 24; i++) {
                ChartVO child = new ChartVO();
                child.setLabel(i+":00");
                child.setStyle("0");
                child.setValue(CustomUtils.getRandomDouble(
                        dto.getParamLowerLimit(),dto.getParamUpperLimit(), dto.getParamBaseValue()
                ));
                items.add(child);
            }
        }else if(dto.getType() == 2) {
            for (int i = 1; i <= 7; i++) {
                ChartVO child = new ChartVO();
                child.setLabel(DateUtil.format(DateUtil.offsetDay(new Date(),i),"yyyy-MM-dd"));
                child.setStyle("0");
                child.setValue(CustomUtils.getRandomDouble(
                        dto.getParamLowerLimit(),dto.getParamUpperLimit(), dto.getParamBaseValue()
                ));
                items.add(child);
            }
        }else{
            for (int i = 1; i <=30 ; i++) {
                ChartVO child = new ChartVO();
                child.setLabel(DateUtil.format(DateUtil.offsetDay(new Date(),i),"yyyy-MM-dd"));
                child.setStyle("0");
                child.setValue(CustomUtils.getRandomDouble(
                        dto.getParamLowerLimit(),dto.getParamUpperLimit(), dto.getParamBaseValue()
                ));
                items.add(child);
            }
        }

        return item.setChilds(items);
    }


}
