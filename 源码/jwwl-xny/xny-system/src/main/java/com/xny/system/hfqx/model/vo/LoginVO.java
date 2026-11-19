package com.xny.system.hfqx.model.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String Code;
    private String Message;
    private HfLoginInfo Data;
}
