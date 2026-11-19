package com.xny.system.controller;

import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.system.model.dto.DataChartDTO;
import com.xny.system.model.dto.DataCountDTO;
import com.xny.system.model.dto.SelectListDTO;
import com.xny.system.model.dto.UpdateDataCountDTO;
import com.xny.system.model.vo.DataChartVO;
import com.xny.system.model.vo.DataCountVO;
import com.xny.system.model.vo.SelectListVO;
import com.xny.system.service.BackManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/backManage")
@Api(tags = "管理后台")
public class BackManageController {
    @Resource
    private BackManageService backManageService;

    @ApiOperation("数据统计-下拉选项")
    @PostMapping("/selectList")
    public R<List<SelectListVO>> selectList(@RequestBody @Validated SelectListDTO dto){
        return backManageService.selectList(dto);
    }

    @ApiOperation("数据统计-分页")
    @PostMapping("/pageDataCount")
    public R<PageInfo<DataCountVO>> pageDataCount(@RequestBody @Validated DataCountDTO dto){
        return backManageService.pageDataCount(dto);
    }

    @ApiOperation("数据修改")
    @PostMapping("/updateDataCount")
    public R<String> updateDataCount(@RequestBody @Validated UpdateDataCountDTO dto){
        return backManageService.updateDataCount(dto);
    }

    @ApiOperation("数据统计-导出")
    @PostMapping("/exportDataCount")
    public void exportDataCount(@RequestBody @Validated DataCountDTO dto, HttpServletResponse response) throws Exception{
        backManageService.exportDataCount(dto,response);
    }


    @ApiOperation("图表-分页")
    @PostMapping("/pageDataChart")
    public R<PageInfo<DataChartVO>> pageDataChart(@RequestBody @Validated DataChartDTO dto){
        return backManageService.pageDataChart(dto);
    }

    @ApiOperation("图表-导出")
    @PostMapping("/exportDataChart")
    public void exportDataChart(@RequestBody @Validated DataChartDTO dto, HttpServletResponse response) throws Exception{
        backManageService.exportDataChart(dto,response);
    }
}
