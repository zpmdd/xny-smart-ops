package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetPlayInfoVO {
    @ApiModelProperty("播放地址")
    private String url;

    @ApiModelProperty("token")
    private String token;
}
