/**
  * Copyright 2024 json.cn 
  */
package com.xny.system.hfqx.model.vo;

import lombok.Data;
import java.util.List;
import java.util.Date;

@Data
public class HfEquipmentInfo {
    private List<String> Children;
    private String Description;
    private int PowerStatus;
    private int Speed;
    private int IsExpired;
    private int IsLocation;
    private int Protocol;
    private String UTCTimeStr;
    private String SpeedStr;
    private double GLat;
    private double GLng;
    private long StopTime;
    private long OfflineTime;
    private String DeviceTypeName;
    private String DeviceModelName;
    private String AlarmContent;
    private String VehicleInfoName;
    private String Address;
    private String WorkStatus;
    private String ConfigTime;
    private String CurrentTime;
    private int NormalStatus;
    private int WaitingTime;
    private int IsStart;
    private int StartTime;
    private int IsEnd;
    private int EndTime;
    private String UserName;
    private String DeviceNumber;
    private String GroupCode;
    private String SIM;
    private int ServicePlan;
    private int DeviceType;
    private int SubType;
    private Date ExpDate;
    private int IsValid;
    private int IsFocus;
    private int ServiceLimit;
    private Date UpdateTime;
    private int IsOnline;
    private String Plate;
    private String DeviceName;
    private String LocationTime;
}