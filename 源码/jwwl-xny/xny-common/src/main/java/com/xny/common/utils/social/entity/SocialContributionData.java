package com.xny.common.utils.social.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 社会贡献数据
 * @author zhuqianwei
 */
@Data
@Accessors(chain = true)
@ToString
public class SocialContributionData {

    private Double standardCoal; // 标煤 单位：吨
    private Double dust; // 碳粉尘 单位：吨
    private Double co2; // 二氧化碳 单位：吨
    private Double arbor; // 树木新增 单位：棵
    private Double mu; // 森林再造 单位：亩
    private Double so2; // 二氧化硫 单位：顿
    private Double nox; // 氮氧化物 单位：顿


}
