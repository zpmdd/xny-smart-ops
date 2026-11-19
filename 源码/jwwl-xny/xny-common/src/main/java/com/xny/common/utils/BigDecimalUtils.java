package com.xny.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BigDecimalUtils {

    /**
     * 两个Double数相加
     * @param v1
     * @return
     */
    public static BigDecimal add(BigDecimal ...v1) {
        BigDecimal result = new BigDecimal(0.0);
        for (BigDecimal bigDecimal : v1) {
            if(bigDecimal != null){
                result = result.add(bigDecimal);
            }
        }
        return new BigDecimal(formatDoubleForTowDecimal(result.doubleValue()) + "");
    }

    /**
     * 保留两位小数，向下取
     * @param finalMoney
     * @return
     */
    public static Double formatDoubleForTowDecimal(Double finalMoney)
    {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(6);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return Double.parseDouble(formater.format(finalMoney));
    }

    /**
     * 保留两位小数，向下取
     * @param finalMoney
     * @return
     */
    public static Double formatDoubleForTowDecimalTwo(Double finalMoney)
    {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return Double.parseDouble(formater.format(finalMoney));
    }

}
