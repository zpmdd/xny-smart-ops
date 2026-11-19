/**
  * Copyright 2024 json.cn 
  */
package com.xny.system.hfqx.model.vo;
import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2024-05-24 16:6:38
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/
 */
@Data
public class HfLoginInfo {

    private String UserCode;
    private String UserName;
    private String LoginName;
    private int UserType;
    private String ContactPerson;
    private String Telephone;
    private String Address;
    private String Email;
    private int TimeZone;
    private String Description;
    private Date CreateTime;
    private Date UpdateTime;
    private String ParentUserName;
    private String ParentTelePhone;
    private String ParentEmail;

}