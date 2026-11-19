/**
  * Copyright 2024 json.cn 
  */
package com.xny.system.ykc.model.dto;
import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2024-05-14 17:11:12
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/
 */
@Data
public class ChargeDetails {
    private Double DetailElecMoney;
    private Date DetailEndTime;
    private Double DetailPower;
    private Double DetailServiceMoney;
    private Double DetailSeviceMoney;
    private Date DetailStartTime;
    private Integer ElecPrice;
    private Integer SevicePrice;
    private String priceType;
}