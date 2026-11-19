package com.xny.system.service;

import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.system.model.dto.DataChartDTO;
import com.xny.system.model.dto.DataCountDTO;
import com.xny.system.model.dto.SelectListDTO;
import com.xny.system.model.dto.UpdateDataCountDTO;
import com.xny.system.model.vo.DataChartVO;
import com.xny.system.model.vo.DataCountVO;
import com.xny.system.model.vo.SelectListVO;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BackManageService {
    R<List<SelectListVO>> selectList(SelectListDTO dto);

    R<PageInfo<DataCountVO>> pageDataCount(DataCountDTO dto);

    R<String> updateDataCount(UpdateDataCountDTO dto);

    void exportDataCount(DataCountDTO dto, HttpServletResponse response)  throws Exception;

    R<PageInfo<DataChartVO>> pageDataChart(DataChartDTO dto);

    void exportDataChart(DataChartDTO dto, HttpServletResponse response)  throws Exception;
}
