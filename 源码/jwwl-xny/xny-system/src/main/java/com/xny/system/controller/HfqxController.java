package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.DetailEquipmentDTO;
import com.xny.system.model.dto.HfqxGlobalViewDTO;
import com.xny.system.model.dto.WindGlobalViewDTO;
import com.xny.system.model.vo.DetailEquipmentVO;
import com.xny.system.model.vo.HfqxGlobalViewVO;
import com.xny.system.model.vo.WindGlobalViewVO;
import com.xny.system.service.IDataHfqxEquipmentService;
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
@RequestMapping("/hfqx")
@Api(tags = "红枫清洗数据")
public class HfqxController {
    @Resource
    private IDataHfqxEquipmentService dataHfqxEquipmentService;

    @ApiOperation("全局总览")
    @PostMapping("/hfqxGlobalView")
    public R<HfqxGlobalViewVO> hfqxGlobalView(@RequestBody @Validated HfqxGlobalViewDTO dto){
        return R.ok(dataHfqxEquipmentService.hfqxGlobalView(dto));
    }

    @ApiOperation("设备详情")
    @PostMapping("/detailEquipment")
    public R<DetailEquipmentVO> detailEquipment(@RequestBody @Validated DetailEquipmentDTO dto){
        return R.ok(dataHfqxEquipmentService.detailEquipment(dto));
    }
}
