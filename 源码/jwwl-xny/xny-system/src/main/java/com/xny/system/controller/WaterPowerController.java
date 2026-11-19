package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.DeviceMonitorDTO;
import com.xny.system.model.dto.WaterGlobalViewDTO;
import com.xny.system.model.vo.DeviceMonitorVO;
import com.xny.system.model.vo.WaterGlobalViewVO;
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
@RequestMapping("/waterPower")
@Api(tags = "水电项目")
public class WaterPowerController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation("全局总览")
    @PostMapping("/waterGlobalView")
    public R<WaterGlobalViewVO> waterGlobalView(@RequestBody @Validated WaterGlobalViewDTO dto){
        return R.ok(sysDeviceService.waterGlobalView(dto));
    }
}
