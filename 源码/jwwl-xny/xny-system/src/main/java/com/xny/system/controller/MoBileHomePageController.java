package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.MapDataCountDTO;
import com.xny.system.model.dto.MoBileHomePageDTO;
import com.xny.system.model.vo.MoBileHomePageVO;
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
@RequestMapping("/moBileHomePage")
@Api(tags = "手机端首页")
public class MoBileHomePageController {
    @Resource
    private ISysDeviceService sysDeviceService;

    @ApiOperation(value = "手机端首页")
    @PostMapping("/moBileHomePage")
    public R<MoBileHomePageVO> moBileHomePage(@RequestBody @Validated MapDataCountDTO dto){
        return R.ok(sysDeviceService.moBileHomePage(dto));
    }
}
