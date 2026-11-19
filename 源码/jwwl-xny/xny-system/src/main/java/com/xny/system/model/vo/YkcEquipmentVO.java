package com.xny.system.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class YkcEquipmentVO {
    @ApiModelProperty("设备id")
    private String connectorID;

    @ApiModelProperty("站点id")
    private String stationID;

    @ApiModelProperty("设备状态(0:空闲,1:使用中,2:故障)")
    private Integer status;

    @ApiModelProperty("电流(A)")
    private BigDecimal currentA;

    @ApiModelProperty("电压(V)")
    private BigDecimal voltageA;

    @ApiModelProperty("充电开始时间")
    private Date startTime;

    @ApiModelProperty("充电结束时间")
    private Date endTime;

    @ApiModelProperty("累计总金额(元)")
    private BigDecimal totalMoney;
}
