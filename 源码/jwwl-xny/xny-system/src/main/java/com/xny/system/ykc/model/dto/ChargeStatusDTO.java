package com.xny.system.ykc.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ChargeStatusDTO {
    private String CarVin;
    private String CardId;
    private Integer ChargeModel;
    private String ConnectorID;
    private Integer ConnectorStatus;
    private Integer CurrentA;
    private Integer DemandCurrent;
    private Integer DemandVoltage;
    private Date EndTime;
    private Integer HighestTemperature;
    private String LeftTime;
    private Integer LowestTemperature;
    private Integer PayMode;
    private Integer Soc;
    private String StartChargeSeq;
    private Integer StartChargeSeqStat;
    private Date StartTime;
    private Integer TotalMoney;
    private Integer TotalPower;
    private String Vin;
    private Integer VoltageA;
}
