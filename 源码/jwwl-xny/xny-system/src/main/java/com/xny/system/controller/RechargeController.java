package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.ChargeStationMonitorDTO;
import com.xny.system.model.dto.DeviceMonitorDTO;
import com.xny.system.model.dto.PilesMonitorDTO;
import com.xny.system.model.vo.ChargeStationMonitorVO;
import com.xny.system.model.vo.DeviceMonitorVO;
import com.xny.system.model.vo.PilesMonitorVO;
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
@RequestMapping("/recharge")
@Api(tags = "充电项目")
public class RechargeController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation("充电桩监测")
    @PostMapping("/chargeStationMonitor")
    public R<ChargeStationMonitorVO> chargeStationMonitor(@RequestBody @Validated ChargeStationMonitorDTO dto){
        return R.ok(sysDeviceService.chargeStationMonitor(dto));
    }

    @ApiOperation("逆变器监测")
    @PostMapping("/pilesMonitor")
    public R<PilesMonitorVO> pilesMonitor(@RequestBody @Validated PilesMonitorDTO dto){
        return R.ok(sysDeviceService.pilesMonitor(dto));
    }
}
