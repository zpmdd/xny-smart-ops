package com.xny.system.sharding;
 
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * 分片算法，按月分片
 */
public class MonthShardingAlgorithm implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
 
    /**
     * 需要空构造方法
     */
    public MonthShardingAlgorithm() {}
 

    /**
     * 精确分片
     * @param collection
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        return buildShardingTable(preciseShardingValue.getLogicTableName(), preciseShardingValue.getValue());
    }
 
    /**
     * 构建分片后的表名
     * @param logicTableName
     * @param date
     * @return
     */
    private String buildShardingTable(String logicTableName, Date date) {
        return logicTableName + "_" + DateUtil.format(date, "yyyy_MM");
    }
 
    /**
     * 范围分片
     * @param collection
     * @param rangeShardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        Date start = valueRange.lowerEndpoint();
        Date end = valueRange.upperEndpoint();
 
        Collection<String> tables = new ArrayList<>();
        while (start.compareTo(end) <= 0) {
            tables.add(buildShardingTable(rangeShardingValue.getLogicTableName(), start));
            start = DateUtil.offsetMonth(start,1);
        }
        
        // collection配置的数据节点表，这里是排除不存在配置中的表
        collection.retainAll(tables);
        return collection;
    }
 
}