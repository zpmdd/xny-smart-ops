package com.xny.system.yzn.model.dto;


import com.xny.system.yzn.model.vo.DatalogVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DatalogDTO extends BaseDTO<DatalogVO>{
    @ApiModelProperty(value = "开始时间(格式yyyy-MM-dd HH:mm:ss)",required = true)
    private String startTime;

    @ApiModelProperty(value = "结束时间(格式yyyy-MM-dd HH:mm:ss)",required = true)
    private String endTime;

    @ApiModelProperty(value = "间隔时间(单位为分钟，不需要按间隔取点，传0即可)",required = true)
    private Integer interval;

    @ApiModelProperty(value = "子项配置",required = true)
    private List<MeterConfigs> meterConfigs;

    @Override
    public String getInterface() {
        return "/device/api/v1/batch/datalog/span/group";
    }
}