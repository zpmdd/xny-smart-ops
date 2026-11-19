package com.xny.system.controller;

import com.xny.common.utils.R;
import com.xny.system.model.dto.ListProjectDTO;
import com.xny.system.model.dto.MapDataCountDTO;
import com.xny.system.model.vo.ListProjectVO;
import com.xny.system.model.vo.ProjectNumberVO;
import com.xny.system.model.vo.MapDataCountVO;
import com.xny.system.service.IDataMapService;
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
@RequestMapping("/mapData")
@Api(tags = "地图数据")
public class DataMapController {
    @Resource
    private IDataMapService dataMapService;

    @ApiOperation(value = "数据统计")
    @PostMapping("/count")
    public R<MapDataCountVO> count(@RequestBody @Validated MapDataCountDTO dto){
        return R.ok(dataMapService.count(dto));
    }

    @ApiOperation(value = "项目列表")
    @PostMapping("/listProject")
    public R<List<ListProjectVO>> listProject(@RequestBody @Validated ListProjectDTO dto){
        return R.ok(dataMapService.listProject(dto));
    }
}
