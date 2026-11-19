package com.xny.system.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetConfigChartDataDTO {
    private Long projectId;
    private Long chartId;
}
