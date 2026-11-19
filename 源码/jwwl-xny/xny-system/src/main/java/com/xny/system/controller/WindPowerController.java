package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.BatteryMonitorDTO;
import com.xny.system.model.dto.BlowerMonitorDTO;
import com.xny.system.model.dto.DeviceMonitorDTO;
import com.xny.system.model.dto.WindGlobalViewDTO;
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
@RequestMapping("/windPower")
@Api(tags = "风电项目")
public class WindPowerController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation("全局总览")
    @PostMapping("/windGlobalView")
    public R<WindGlobalViewVO> windGlobalView(@RequestBody @Validated WindGlobalViewDTO dto){
        return R.ok(sysDeviceService.windGlobalView(dto));
    }

    @ApiOperation("风机监测")
    @PostMapping("/blowerMonitor")
    public R<BlowerMonitorVO> blowerMonitor(@RequestBody @Validated BlowerMonitorDTO dto){
        return R.ok(sysDeviceService.blowerMonitor(dto));
    }
}
