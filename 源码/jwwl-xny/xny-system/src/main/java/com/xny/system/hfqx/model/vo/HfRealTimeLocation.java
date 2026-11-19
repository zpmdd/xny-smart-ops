/**
  * Copyright 2024 json.cn 
  */
package com.xny.system.hfqx.model.vo;
import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2024-05-24 15:59:28
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/
 */

@Data
public class HfRealTimeLocation {
    private String RobotWorkStatus;
    private String ConfigTime;
    private String CurrentTime;
    private int WaitingTime;
    private int IsStart;
    private int IsStartRun;
    private int StartTime;
    private int IsEnd;
    private int EndTime;
    private int IsEndRun;
    private int T0Value;
    private int T1Value;
    private int T2Value;
    private int TValue;
    private int CurrentControl;
    private int LocationID;
    private String DeviceNumber;
    private int DeviceType;
    private Date UTCTime;
    private String SIM;
    private int IsValid;
    private double GLat;
    private double GLng;
    private double OLat;
    private double OLng;
    private double BLat;
    private double BLng;
    private int Speed;
    private int Course;
    private int IsStop;
    private int Distance;
    private long StopTime;
    private String Address;
    private Date LocalTime;
    private int PowerStatus;
    private int IsOnline;
    private String Plate;
    private String GroupName;
    private int BAT;
    private String ReportInterval;
    private int RecordCnt;
    private int LocationModel;
    private String AlarmContent;
    private String TimePoint;
    private int DeviceStatus;
    private int IsDisplaySetup;
}