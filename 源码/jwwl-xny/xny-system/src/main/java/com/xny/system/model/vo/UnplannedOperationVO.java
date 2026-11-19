package com.xny.system.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UnplannedOperationVO {
    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("电站下告警设备uuid")
    private String uuid;

    @ApiModelProperty("告警设备类型，如：station、elink、eblock、meter")
    private String type;

    @ApiModelProperty("告警具体设备如：eblock、PCS名称")
    private String name;

    @ApiModelProperty("告警属于那个电站")
    private String stationId;

    @ApiModelProperty("告警触发的时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("告警关闭的时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;;

    @ApiModelProperty("告警类型（1:离线告警,2:非计划运行告警）")
    private Integer warnType = 2;
}
