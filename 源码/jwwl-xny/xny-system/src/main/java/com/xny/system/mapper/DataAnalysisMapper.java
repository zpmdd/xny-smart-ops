package com.xny.system.mapper;

import com.xny.system.model.dto.MapDataCountDTO;
import com.xny.system.model.vo.ProjectNumberVO;
import com.xny.system.model.vo.ProjectDataCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataAnalysisMapper {
    List<ProjectDataCountVO> projectDataCount(MapDataCountDTO dto);

    List<ProjectNumberVO> projectNumber(MapDataCountDTO dto);

    String projectDimension(@Param(value = "ids") String ids);
}
