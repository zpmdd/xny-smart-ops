package com.xny.system.model.dto;

import com.xny.system.model.vo.ProjectDataCountVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MapDataCountDTO {
    @ApiModelProperty(value = "项目类型id")
    private Long projectTypeId;

    @ApiModelProperty(value = "项目类型id集合")
    private List<Long> projectTypeIdList;

    @ApiModelProperty(value = "项目id集合,逗号分隔")
    private String projectIds;

    @ApiModelProperty(value = "省级编码")
    private String provinceCode;

    @ApiModelProperty(value = "参数id集合",hidden = true)
    private List<Long> paramIdList;

    @ApiModelProperty(value = "参数id",hidden = true)
    private Long paramId;
}
