package com.xny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xny.common.enums.ChartTypeEnum;
import com.xny.common.utils.ExportUtils;
import com.xny.common.utils.R;
import com.xny.system.entity.DataParamConfig;
import com.xny.system.entity.SysParamSimulate;
import com.xny.system.mapper.DataParamConfigMapper;
import com.xny.system.mapper.SysParamSimulateMapper;
import com.xny.system.model.dto.DataChartDTO;
import com.xny.system.model.dto.DataCountDTO;
import com.xny.system.model.dto.SelectListDTO;
import com.xny.system.model.dto.UpdateDataCountDTO;
import com.xny.system.model.vo.DataChartVO;
import com.xny.system.model.vo.DataCountVO;
import com.xny.system.model.vo.SelectListVO;
import com.xny.system.service.BackManageService;
import com.xny.system.service.IDataParamConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BackManageServiceImpl implements BackManageService {
    @Resource
    private IDataParamConfigService dataParamConfigService;

    @Resource
    private DataParamConfigMapper dataParamConfigMapper;

    @Resource
    private SysParamSimulateMapper sysParamSimulateMapper;

    @Override
    public R<List<SelectListVO>> selectList(SelectListDTO dto) {
        List<DataParamConfig> list = dataParamConfigService.list(new LambdaQueryWrapper<DataParamConfig>()
                .eq(DataParamConfig::getProjectId, dto.getProjectType().getId())
                .eq(DataParamConfig::getDataType,1)
        );
        List<SelectListVO> selectListVOS = CglibUtil.copyList(list, SelectListVO::new);
        return R.ok(selectListVOS);
    }

    @Override
    public R<PageInfo<DataCountVO>> pageDataCount(DataCountDTO dto) {
        dto.setProjectId(dto.getProjectType().getId());

        if(null != dto.getEndTime()){
            dto.setEndTime(DateUtil.endOfDay(dto.getEndTime()));
        }

        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
        List<DataCountVO> dataCountVOS = dataParamConfigMapper.listDataParamConfig(dto);
        for (DataCountVO dataCountVO : dataCountVOS) {
            if(null != dataCountVO.getParamId() && dataCountVO.getParamId().equals(100020L)){
                dataCountVO.setParamValue(new BigDecimal(dataCountVO.getParamValue()).multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString());
            }
            if(dataCountVO.getId().equals(100113L)){
                dataCountVO.setIfNumber(false);
            }
        }
        return R.ok(new PageInfo<>(dataCountVOS));
    }

    @Override
    public R<String> updateDataCount(UpdateDataCountDTO dto) {
        DataParamConfig dataParamConfig = dataParamConfigService.getById(dto.getId());
        if(BeanUtil.isEmpty(dataParamConfig)){
            return R.failed("未查询到参数信息");
        }

        if(null != dto.getParamValue() && null != dataParamConfig.getParamId()){
            if(dataParamConfig.getParamId().equals(100020L)){
                dto.setParamValue(new BigDecimal(dto.getParamValue())
                        .divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());

            }else if(dataParamConfig.getParamId().equals(110004L)){
                SysParamSimulate swSimulate = sysParamSimulateMapper.selectById(101L);
                SysParamSimulate sySimulate = sysParamSimulateMapper.selectById(102L);

                String tatalSw = new BigDecimal(dto.getParamValue()).multiply(BigDecimal.valueOf(swSimulate.getParamBaseValue())).stripTrailingZeros().toPlainString();
                String tatalSy = new BigDecimal(dto.getParamValue()).multiply(BigDecimal.valueOf(sySimulate.getParamBaseValue())).divide(BigDecimal.valueOf(10000),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();

                dataParamConfigService.update(new DataParamConfig()
                                .setParamValue(tatalSw),
                        new LambdaUpdateWrapper<DataParamConfig>()
                            .eq(DataParamConfig::getProjectId,dataParamConfig.getProjectId())
                            .eq(DataParamConfig::getParamId,100008L)
                );

                dataParamConfigService.update(new DataParamConfig()
                                .setParamValue(tatalSy),
                        new LambdaUpdateWrapper<DataParamConfig>()
                                .eq(DataParamConfig::getProjectId,dataParamConfig.getProjectId())
                                .eq(DataParamConfig::getParamId,100005L)
                );

            }

        }else if(null != dto.getIfEnable() && null != dataParamConfig.getParamId() && dataParamConfig.getParamId().equals(110004L)){
                dataParamConfigService.update(new DataParamConfig()
                                .setIfEnable(dto.getIfEnable()),
                        new LambdaUpdateWrapper<DataParamConfig>()
                                .eq(DataParamConfig::getProjectId,dataParamConfig.getProjectId())
                                .eq(DataParamConfig::getParamId,100008L)
                );

                dataParamConfigService.update(new DataParamConfig()
                                .setIfEnable(dto.getIfEnable()),
                        new LambdaUpdateWrapper<DataParamConfig>()
                                .eq(DataParamConfig::getProjectId,dataParamConfig.getProjectId())
                                .eq(DataParamConfig::getParamId,100005L)
                );
        }

        DataParamConfig configForUpdate = new DataParamConfig();
        configForUpdate.setId(dto.getId());
        configForUpdate.setIfEnable(dto.getIfEnable());
        configForUpdate.setParamValue(dto.getParamValue());
        dataParamConfigService.updateById(configForUpdate);

        return R.ok("操作成功");
    }

    @Override
    public R<PageInfo<DataChartVO>> pageDataChart(DataChartDTO dto) {
        dto.setProjectId(dto.getProjectType().getId());

        if(null != dto.getEndTime()){
            dto.setEndTime(DateUtil.endOfDay(dto.getEndTime()));
        }

        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
        List<DataChartVO> dataChartVOS = dataParamConfigMapper.listDataParamConfig2(dto);

        ChartTypeEnum chartTypeEnum = ChartTypeEnum.getById(dto.getChartId());
        for (DataChartVO dataChartVO : dataChartVOS) {
            dataChartVO.setChartName(chartTypeEnum.getRemark());
        }
        return R.ok(new PageInfo<>(dataChartVOS));
    }

    @Override
    public void exportDataCount(DataCountDTO dto, HttpServletResponse response)  throws Exception{
        dto.setPageNum(1);
        dto.setPageSize(Integer.MAX_VALUE);
        R<PageInfo<DataCountVO>> pageInfoR = this.pageDataCount(dto);
        List<DataCountVO> records = pageInfoR.getData().getList();

        List<String> heads = Arrays.asList("数据名称","数据","单位","是否以配置数据为准","操作人","操作时间");

        List<List<String>> contents = new ArrayList<>();
        for (DataCountVO record : records) {
            List<String> content = Lists.newArrayList();
            content.add(record.getParamName());
            content.add(record.getParamValue());
            content.add(record.getParamUnit());
            content.add(record.getIfEnable() == 1 ? "是":"否");
            content.add(record.getUpdateName());
            content.add(null == record.getUpdateTime() ? "":DateUtil.format(record.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
            contents.add(content);
        }

        ExportUtils.exportSimpleHead(response,heads,contents,"数据统计列表");
    }

    @Override
    public void exportDataChart(DataChartDTO dto, HttpServletResponse response) throws Exception{
        dto.setPageNum(1);
        dto.setPageSize(Integer.MAX_VALUE);
        R<PageInfo<DataChartVO>> pageInfoR = this.pageDataChart(dto);
        List<DataChartVO> records = pageInfoR.getData().getList();

        List<String> heads = Arrays.asList("图表名称","图例名称","单位","横轴","数值","操作人","操作时间");
        List<List<String>> contents = new ArrayList<>();

        for (DataChartVO record : records) {
            List<String> content = Lists.newArrayList();
            content.add(record.getChartName());
            content.add(record.getParamName());
            content.add(record.getParamUnit());
            content.add(record.getParamKey());
            content.add(record.getParamValue());
            content.add(record.getUpdateName());
            content.add(null == record.getUpdateTime() ? "":DateUtil.format(record.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
            contents.add(content);
        }

        ExportUtils.exportSimpleHead(response,heads,contents,"图表列表");
    }


}
