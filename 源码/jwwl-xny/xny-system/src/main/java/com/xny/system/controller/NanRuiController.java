package com.xny.system.controller;


import com.xny.common.utils.R;
import com.xny.system.model.dto.DataRecieveDTO;
import com.xny.system.service.NanRuiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@RestController
@RequestMapping("/nanRui")
@Api(tags = "南瑞数据")
public class NanRuiController {
    @Resource
    private NanRuiService nanRuiService;

    @ApiOperation("数据接收")
    @PostMapping("/dataRecieve")
    public R<String> dataRecieve(@RequestBody @Validated DataRecieveDTO dto){
        return nanRuiService.dataRecieve(dto);
    }
}
