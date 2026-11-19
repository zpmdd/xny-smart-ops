package com.xny.system.controller;

import com.xny.system.service.QdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/qdData")
@Api(tags = "奇点数据")
public class QdController {
    @Resource
    private QdService qdService;

    @ApiOperation("获取奇点数据")
    @PostMapping("/getQdData")
    public void getQdData(){
        qdService.getQdData();
    }
}
