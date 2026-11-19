package com.xny.common.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;

public class CustomUtils {
    /**
     * 正则去除.或多余的0
     *
     * @param num 需要去除的参数
     */
    public static String removeZeros(String num) {
        if (StrUtil.isNotBlank(num) && NumberUtil.isDouble(num)) {
            BigDecimal bigDecimal = new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
            return bigDecimal.toPlainString();
        }
        return num;
    }

    /**
     * 生成随机double
     * @param minValue
     * @param maxValue
     * @return
     */
    public static double getRandomDouble(Double minValue,Double maxValue,Double baseValue) {
        if(null == minValue || null == maxValue){
            return baseValue;
        }
        return  RandomUtil.randomDouble(minValue,maxValue) + baseValue;
    }
}
