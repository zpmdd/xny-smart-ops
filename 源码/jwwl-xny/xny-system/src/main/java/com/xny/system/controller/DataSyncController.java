package com.xny.system.controller;

import com.xny.system.model.dto.CompleDataDTO;
import com.xny.system.model.dto.RealTimeDataDTO;
import com.xny.system.model.dto.YznDataLogDTO;
import com.xny.system.service.DataSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/dataSync")
@Api(tags = "数据同步")
public class DataSyncController {
    @Resource
    private DataSyncService dataSyncService;

    @ApiOperation("历史数据同步")
    @PostMapping("/yznDataLog")
    public void dataLog(@RequestBody @Validated YznDataLogDTO dto){
        dataSyncService.dataLog(dto);
    }

    @ApiOperation("实时数据同步")
    @PostMapping("/realTimeData")
    public void realTimeData(@RequestBody @Validated RealTimeDataDTO dto){
        dataSyncService.realTimeData(dto);
    }

    @ApiOperation("数据补全")
    @PostMapping("/compleData")
    public void compleData(CompleDataDTO dto){
        dataSyncService.compleData(dto);
    }
}
