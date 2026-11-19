package com.xny.system.model.dto;

import com.xny.common.enums.ProjectTypeEnum;
import com.xny.system.model.vo.ChartDataVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChartItemInfoDTO {
    @ApiModelProperty(value = "项目类型")
    private ProjectTypeEnum projectType;

    @ApiModelProperty(value = "参数id")
    private Long paramId;

    @ApiModelProperty(value = "参数id集合")
    private List<Long> paramIdList;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty(value = "数据项名称")
    private String itemName;

    @ApiModelProperty(value = "数据项风格")
    private String itemStyle;

    @ApiModelProperty(value = "数据项单位")
    private String itemUnit;

    @ApiModelProperty(value = "数据集合")
    private List<ChartDataVO> dataList;

    @ApiModelProperty(value = "计算增量")
    private boolean ifIncrement = false;

    @ApiModelProperty(value = "计算同比增长")
    private boolean ifYoy = false;
}
