package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.DataStatisticsDTO;
import com.xny.system.model.dto.PvGlobalOverviewDTO;
import com.xny.system.model.dto.InverterMonitoringVO;
import com.xny.system.model.dto.SystemArchitectureDTO;
import com.xny.system.model.vo.*;
import com.xny.system.service.ISysDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/dcsPv")
@Api(tags = "分布式光伏")
public class DcsPvController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation("全局总览")
    @PostMapping("/pvGlobalOverview")
    public R<PvGlobalOverviewVO> globalOverview(@RequestBody @Validated PvGlobalOverviewDTO dto){
        return R.ok(sysDeviceService.pvGlobalOverview(dto));
    }

    @ApiOperation("系统架构")
    @PostMapping("/systemArchitecture")
    public R<SystemArchitectureVO> systemArchitecture(@RequestBody @Validated SystemArchitectureDTO dto){
        return R.ok(sysDeviceService.systemArchitecture(dto));
    }

    @ApiOperation("逆变器监测")
    @PostMapping("/inverterMonitoring")
    public R<InverterMonitoringVO> inverterMonitoring(@RequestBody @Validated InverterMonitoringDTO dto){
        return R.ok(sysDeviceService.inverterMonitoring(dto));
    }

    @ApiOperation("数据统计")
    @PostMapping("/dataStatistics")
    public R<DataStatisticsVO> dataStatistics(@RequestBody @Validated DataStatisticsDTO dto){
        return R.ok(sysDeviceService.dataStatistics(dto));
    }
}
