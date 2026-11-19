package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.entity.SysDict;
import com.xny.system.model.dto.ListByTypeDTO;
import com.xny.system.service.ISysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sysDict")
@Api(tags = "字典数据管理")
public class SysDictController {
    @Resource
    private ISysDictService sysDictService;

    @ApiOperation(value = "按类型查询字典列表")
    @PostMapping("/listByType")
    public R<List<SysDict>> listByType(@RequestBody @Validated ListByTypeDTO dto){
        return R.ok(sysDictService.listByType(dto));
    }

}
