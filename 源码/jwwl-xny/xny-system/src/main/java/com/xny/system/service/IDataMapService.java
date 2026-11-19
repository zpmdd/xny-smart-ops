package com.xny.system.service;

import com.xny.common.enums.GeneralParamEnum;
import com.xny.system.model.dto.ListProjectDTO;
import com.xny.system.model.dto.MapDataCountDTO;
import com.xny.system.model.vo.ItemVO;
import com.xny.system.model.vo.ListProjectVO;
import com.xny.system.model.vo.MapDataCountVO;

import java.util.List;

public interface IDataMapService {
    MapDataCountVO count(MapDataCountDTO dto);

    List<ListProjectVO> listProject(ListProjectDTO dto);

    ItemVO socialBenefit(Double pgc);

    ItemVO otherCount(MapDataCountDTO dto , GeneralParamEnum paramEnum);

    ItemVO totalItems(MapDataCountDTO dto);

}
