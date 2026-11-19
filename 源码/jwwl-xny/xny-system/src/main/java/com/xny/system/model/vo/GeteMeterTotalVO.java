package com.xny.system.model.vo;

import lombok.Data;

@Data
public class GeteMeterTotalVO {
    private String meterType;
    private String meterName;
    private String id;
    private String uuid;
    private String u_ab;
    private String u_bc;
    private String u_ca;
    private String i_a;
    private String i_b;
    private String i_c;
    private String p_a;
    private String p_b;
    private String p_c;
    private String p_total;
    private String q_a;
    private String q_b;
    private String q_c;
    private String q_total;
    private String pf_a;
    private String pf_b;
    private String pf_c;
    private String pf;
    private String f;
    private String e_positive;
    private String e_reverse;
    private String e_positive_sharp;
    private String e_positive_peak;
    private String e_positive_flat;
    private String e_positive_valley;
    private String e_reverse_sharp;
    private String e_reverse_peak;
    private String e_reverse_flat;
    private String e_reverse_valley;
    private String record_time;
}
