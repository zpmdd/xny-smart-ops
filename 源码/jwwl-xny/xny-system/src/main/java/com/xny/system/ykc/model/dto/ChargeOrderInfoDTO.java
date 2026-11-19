/**
  * Copyright 2024 json.cn 
  */
package com.xny.system.ykc.model.dto;
import lombok.Data;

import java.util.List;
import java.util.Date;

/**
 * Auto-generated: 2024-05-14 17:11:12
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/
 */
@Data
public class ChargeOrderInfoDTO {

    private String CardId;
    private List<ChargeDetails> ChargeDetails;
    private Integer ChargeEndSoc;
    private Integer ChargeEndValue;
    private Integer ChargeLast;
    private Integer ChargeModel;
    private Integer ChargeStartSoc;
    private Integer ChargeStartValue;
    private String ConnectorID;
    private Integer ConnectorPower;
    private String ElecmeterVersion;
    private String EncryType;
    private String EndSoc;
    private Date EndTime;
    private String EquipmentID;
    private String MeterNum;
    private Integer MeterNumEnd;
    private Integer MeterNumStart;
    private Integer MeterValueEnd;
    private Integer MeterValueStart;
    private String RawData;
    private String StartChargeSeq;
    private String StartSoc;
    private Date StartTime;
    private String StationID;
    private Integer StopReason;
    private Integer SumPeriod;
    private Double TotalElecMoney;
    private Double TotalMoney;
    private Double TotalPower;
    private Double TotalSeviceMoney;
    private String TradeSeq;
    private String UserFlag;
    private String UserID;
    private String UserName;
}