package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.vo.GetPlayInfoVO;
import com.xny.system.yingshi.utils.YsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yingShi")
@Api(tags = "萤石视频")
public class YingShiController {

    @ApiOperation("获取视频播放地址")
    @PostMapping("/getPlayInfo")
    public R<GetPlayInfoVO> getPlayInfo(){
        GetPlayInfoVO playInfoVO = new GetPlayInfoVO();
        playInfoVO.setUrl(YsUtils.getPlayUrl());
        playInfoVO.setToken(YsUtils.getToken());
        return R.ok(playInfoVO);
    }
}
