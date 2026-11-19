package com.xny.system.yzn.model.dto;


import com.xny.system.yzn.model.vo.RealtimeDataVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RealtimeDataDTO extends BaseDTO<RealtimeDataVO> {
    @ApiModelProperty(value = "厂站id（如何获取看请求响应示例）")
    private Integer stationId;

    @ApiModelProperty(value = "通道id")
    private Integer channelId;

    @ApiModelProperty(value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(value = "测点id")
    private Integer measureId;


    @Override
    public String getInterface() {
        return "device/api/v1/realtimedata";
    }
}
