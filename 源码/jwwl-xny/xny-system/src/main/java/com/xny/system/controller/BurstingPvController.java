package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.*;
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
import java.util.List;

@RestController
@RequestMapping("/burstingPv")
@Api(tags = "集中式光伏")
public class BurstingPvController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation("全局总览")
    @PostMapping("/burstingGlobalView")
    public R<BurstingGlobalViewVO> burstingGlobalView(@RequestBody @Validated BurstingGlobalViewDTO dto){
        return R.ok(sysDeviceService.burstingGlobalView(dto));
    }

    @ApiOperation("逆变器监测")
    @PostMapping("/bpvInverterMonitoring")
    public R<BpvInverterMonitoringVO> bpvInverterMonitoring(@RequestBody @Validated BpvInverterMonitoringDTO dto){
        return R.ok(sysDeviceService.bpvInverterMonitoring(dto));
    }

    @ApiOperation("数据统计")
    @PostMapping("/burstingDataStatistics")
    public R<BurstingDataStatisticsVO> burstingDataStatistics(@RequestBody @Validated BurstingDataStatisticsDTO dto){
        return R.ok(sysDeviceService.burstingDataStatistics(dto));
    }

    @ApiOperation("子阵列表")
    @PostMapping("/listSubmatrix")
    public R<List<String>> listSubmatrix(){
        return R.ok(sysDeviceService.listSubmatrix());
    }
}
