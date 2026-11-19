package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.BatteryMonitorDTO;
import com.xny.system.model.dto.DeviceMonitorDTO;
import com.xny.system.model.vo.BatteryMonitorVO;
import com.xny.system.model.vo.DeviceMonitorVO;
import com.xny.system.model.vo.NewDeviceMonitorVO;
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
@RequestMapping("/storage")
@Api(tags = "储能项目")
public class StorageController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation("储能设备监测")
    @PostMapping("/deviceMonitor")
    public R<DeviceMonitorVO> deviceMonitor(@RequestBody @Validated DeviceMonitorDTO dto){
        return R.ok(sysDeviceService.deviceMonitor(dto));
    }

    @ApiOperation("储能电池监测")
    @PostMapping("/batteryMonitor")
    public R<BatteryMonitorVO> batteryMonitor(@RequestBody @Validated BatteryMonitorDTO dto){
        return R.ok(sysDeviceService.batteryMonitor(dto));
    }

    @ApiOperation("储能设备监测(新)")
    @PostMapping("/newDeviceMonitor")
    public R<NewDeviceMonitorVO> newDeviceMonitor(@RequestBody @Validated DeviceMonitorDTO dto){
        return R.ok(sysDeviceService.newDeviceMonitor(dto));
    }
}
