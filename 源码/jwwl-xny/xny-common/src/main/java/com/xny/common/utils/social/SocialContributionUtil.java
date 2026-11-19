package com.xny.common.utils.social;

import com.xny.common.utils.social.entity.SocialContributionData;

import java.math.BigDecimal;

/**
 * 社会贡献获取工具类
 *
 * @author qianwei
 */

public class SocialContributionUtil {

    /**
     * 获取社会贡献
     *
     * @param powerGeneration 单位kWh（度）
     * @return SocialContributionData
     */
    public static SocialContributionData getData(Long powerGeneration) {
        Double standardCoalCoefficient = 0.4; // 标煤系数
        Double dustCoefficient = 0.272; // 碳粉尘 单位：吨
        Double co2Coefficient = 0.000997; // 二氧化碳 单位：吨
        Double arborCoefficient = 0.48; // 树木新增 单位：棵
        Double muCoefficient = 50.0; // 森林再造 单位：亩
        Double so2Coefficient = 0.00003;
        Double noxCoefficient = 0.000015;
        int tonCoefficient = 1000; // 吨系数
        standardCoalCoefficient = 0.00036; // 标煤系数
        dustCoefficient = 0.000272004860792; // 碳粉尘 单位：吨
        //co2Coefficient = 0.000790098; // 二氧化碳 单位：吨
        arborCoefficient = 0.043200002263465; // 树木新增 单位：棵
        muCoefficient = 0.000407423741558; // 森林再造 单位：亩
        so2Coefficient = 0.00003; // 二氧化碳
        noxCoefficient = 0.000015;
        tonCoefficient = 1; // 吨系数
        SocialContributionData result = new SocialContributionData();
        if (powerGeneration != null) {
            result.setStandardCoal(getDoubleValue(powerGeneration * standardCoalCoefficient / tonCoefficient)) // 一度电节省 0.4 千克标煤
                    .setDust(getDoubleValue(powerGeneration * dustCoefficient / tonCoefficient)) // 一度减排 0.272 千克碳粉尘
                    .setCo2(getDoubleValue(powerGeneration * co2Coefficient / tonCoefficient)) // 一度减排 0.997 千克二氧化碳
                    .setArbor(getDoubleValue(result.getStandardCoal() / arborCoefficient)) // 0.48吨标煤约造一棵树
                    .setMu(getDoubleValue(result.getArbor() / muCoefficient)); // 一亩地约50棵树

//            result.setStandardCoal(getDoubleValue(powerGeneration * standardCoalCoefficient)) // 标煤
//                    .setDust(getDoubleValue(powerGeneration * dustCoefficient)) //碳粉尘
//                    .setCo2(getDoubleValue(powerGeneration * co2Coefficient)) // 二氧化碳
//                    .setArbor(getDoubleValue(powerGeneration * arborCoefficient)) // 造一棵树
//                    .setMu(getDoubleValue(powerGeneration * muCoefficient)) // 亩地
//                    .setSo2(getDoubleValue(powerGeneration * so2Coefficient))
//                    .setNox(getDoubleValue(powerGeneration * noxCoefficient));
        } else {
            result.setStandardCoal(0.0)
                    .setDust(0.0)
                    .setCo2(0.0)
                    .setArbor(0.0)
                    .setMu(0.0);
        }
        return result;
    }

    /**
     * 保留3位小数
     *
     * @param value 值
     * @return Double
     */
    private static Double getDoubleValue(Double value) {
        return new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
