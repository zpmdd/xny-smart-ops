package com.xny.system.sharding;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class DayShardingAlgorithm implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
 
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        String db_name = preciseShardingValue.getLogicTableName();
        try {
            Date date = preciseShardingValue.getValue();
            return buildShardingTable(db_name,date);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        throw new IllegalArgumentException();
    }

    /**
     * 构建分片后的表名
     * @param logicTableName
     * @param date
     * @return
     */
    private String buildShardingTable(String logicTableName, Date date) {
        String year = String.format("%tY", date);
        String mon = String.format("%tm",date);
        String day = String.format("%td",date);
        return logicTableName+"_"+year+mon+day;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        Date start = valueRange.lowerEndpoint();
        Date end = valueRange.upperEndpoint();

        Collection<String> tables = new ArrayList<>();
        while (start.compareTo(end) <= 0) {
            tables.add(buildShardingTable(rangeShardingValue.getLogicTableName(), start));
            start = DateUtil.offsetDay(start,1);
        }

        // collection配置的数据节点表，这里是排除不存在配置中的表
        collection.retainAll(tables);
        return collection;
    }
}