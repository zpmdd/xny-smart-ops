package com.xny.system.service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xny.common.config.XNYConfig;
import com.xny.common.utils.CustomUtils;
import com.xny.common.utils.SnowIdUtil;
import com.xny.system.model.dto.CompleDataDTO;
import com.xny.system.entity.*;
import com.xny.system.mapper.SysParamSimulateMapper;
import com.xny.system.model.dto.MeterConfigDTO;
import com.xny.system.model.dto.RealTimeDataDTO;
import com.xny.system.model.dto.UpdateAlarmCountDTO;
import com.xny.system.model.dto.YznDataLogDTO;
import com.xny.system.model.vo.DataSyncVO;
import com.xny.system.service.*;
import com.xny.system.yzn.model.dto.DatalogDTO;
import com.xny.system.yzn.model.dto.MeterConfigs;
import com.xny.system.yzn.model.dto.RealtimeDataDTO;
import com.xny.system.yzn.model.vo.*;
import com.xny.system.yzn.utils.RequestUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataSyncServiceImpl implements DataSyncService {

    @Resource
    private SysParamSimulateMapper sysParamSimulateMapper;

    @Resource
    private IDataDeviceRealTimeService dataDeviceRealTimeService;

    @Resource
    private XNYConfig xnyConfig;

    @Resource
    private ISysDeviceService sysDeviceService;

    @Resource
    private ISysParamService sysParamService;

    //记录小时数据参数id集合
    private final List<Long> recordHourList = Arrays.asList(110021L,100010L,110003L,120002L,120035L,120036L,130001L,130033L);
    //记录月参数id集合
    private final List<Long> recordMonthList = Arrays.asList(110041L,110021L);
    //计算收益参数id集合
    private final List<Long> calculateSyList = Arrays.asList(110003L,110041L,110004L);
    //计算收益设备id集合
    private final List<Long> deviceIdList = Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L,9L,13L);
    //计算告警次数参数id集合
    private final List<Long> alarmList = Arrays.asList(120100L,120106L,120105L,120107L,120108L,120164L,120134L,120113L,120133L,120115L,120162L,120153L);
    //计算告警次数参数id集合
    private final List<Long> alarmCountList = Arrays.asList(100023L,100024L);

    //上网计算因数
    private Double swFactor = 0D;
    //收益计算因数
    private Double syFactor = 0D;

    @Override
    public void dataLog(YznDataLogDTO dto) {
        setFactor();
        dataLogForFbsgf(dto);
        dataLogForJzsgf(dto);
    }

    /**
     * 获取最新 发电量 与 上网 收益 计算系数
     */
    private void setFactor(){
        SysParamSimulate swSimulate = sysParamSimulateMapper.selectById(101L);
        SysParamSimulate sySimulate = sysParamSimulateMapper.selectById(102L);
        swFactor = swSimulate.getParamBaseValue();
        syFactor = sySimulate.getParamBaseValue();
    }

    /**
     * 分布式光伏历史数据处理
     * @param dto
     */
    private void dataLogForFbsgf(YznDataLogDTO dto) {
        //设备id集合
        List<Long> deviceIds = Arrays.asList(1L,2L,3L,4L,5L,6L,7L,13L);
        //云综能设备id集合
        List<Long> yznDeviceIds = Arrays.asList(30L,31L,32L,33L,34L,35L,36L,50L);
        //云综能参数id集合
        List<Long> yznParamIds = Arrays.asList(6000254L,2000004L);

        List<MeterConfigDTO> paramList = getParamList(yznDeviceIds,yznParamIds);
        List<Map<String, String>> monthList = getMonthList();

        Map<String, SysParamSimulate> simulateMap = sysParamSimulateMapper.selectList(new LambdaQueryWrapper<SysParamSimulate>()
                .in(SysParamSimulate::getYznParamId,yznParamIds)
                .in(SysParamSimulate::getYznDeviceId,deviceIds)
        ).stream().collect(Collectors.toMap(t -> t.getYznDeviceId() + "" + t.getYznParamId(), t -> t));

        List<DataDeviceMonth> monthDataList = new ArrayList<>();

        for (Map<String, String> map : monthList) {
            DatalogDTO datalogDTO = new DatalogDTO();
            datalogDTO.setStartTime(map.get("startTime"));
            datalogDTO.setEndTime(map.get("endTime"));
            datalogDTO.setInterval(5);

            List<MeterConfigs> meterConfigList = new ArrayList<>();
            for (MeterConfigDTO meterConfigDTO : paramList) {
                MeterConfigs meterConfigs = new MeterConfigs();
                meterConfigs.setDataId(meterConfigDTO.getParamId().intValue());//参数ID
                meterConfigs.setDataTypeId(1);
                meterConfigs.setDeviceId(meterConfigDTO.getDeviceId().intValue());//设备ID
                meterConfigs.setLogicalId(1);
                meterConfigList.add(meterConfigs);
            }
            datalogDTO.setMeterConfigs(meterConfigList);

            DatalogVO send;
            if(xnyConfig.getUseSimulateData()){
                send = dataLogSimulation(datalogDTO,simulateMap);
            }else {
                send = RequestUtils.doSend(datalogDTO, DatalogVO.class);
            }

            if(send.getCode()==0){
                Map<Integer, Long> tempMap = getTempMap();

                for (DatalogDetailVO datum : send.getData()) {
                    if(CollectionUtil.isEmpty(datum.getDataList())){
                        continue;
                    }
                    DataDeviceMonth dataDeviceMonth = new DataDeviceMonth();
                    dataDeviceMonth.setId(SnowIdUtil.nextId());
                    dataDeviceMonth.setDeviceId(tempMap.get(datum.getDeviceId()));
                    dataDeviceMonth.setParamId(tempMap.get(datum.getDataId()));


                    DatalogValueVO datalogValueVO = getDatalogValueVO(datum);
                    if(BeanUtil.isEmpty(datalogValueVO)){
                        continue;
                    }

                    dataDeviceMonth.setParamValue(datalogValueVO.getValue());
                    dataDeviceMonth.setParamTime(DateUtil.parse(datalogValueVO.getTimeStr(),"yyyy-MM"));
                    monthDataList.add(dataDeviceMonth);

                    if(dataDeviceMonth.getParamId().equals(110041L)){
                        //根据月发电量计算月收益（万元）
                        DataDeviceMonth dataDeviceMonth2 = new DataDeviceMonth();
                        dataDeviceMonth2.setId(SnowIdUtil.nextId());
                        dataDeviceMonth2.setDeviceId(tempMap.get(datum.getDeviceId()));
                        dataDeviceMonth2.setParamId(100003L);
                        dataDeviceMonth2.setParamValue(datalogValueVO.getValue()*syFactor/10000);
                        dataDeviceMonth2.setParamTime(DateUtil.parse(datalogValueVO.getTimeStr(),"yyyy-MM"));
                        monthDataList.add(dataDeviceMonth2);

                        //根据月发电量计算上网
                        DataDeviceMonth dataDeviceMonth3 = new DataDeviceMonth();
                        dataDeviceMonth3.setId(SnowIdUtil.nextId());
                        dataDeviceMonth3.setDeviceId(tempMap.get(datum.getDeviceId()));
                        dataDeviceMonth3.setParamId(100008L);
                        dataDeviceMonth3.setParamValue(datalogValueVO.getValue()*swFactor);
                        dataDeviceMonth3.setParamTime(DateUtil.parse(datalogValueVO.getTimeStr(),"yyyy-MM"));
                        monthDataList.add(dataDeviceMonth3);
                    }
                }
            }
        }

        if(CollectionUtil.isNotEmpty(monthDataList)){
            sysParamSimulateMapper.monthBatchInsertOrUpdate(monthDataList);
        }
    }

    private DatalogValueVO getDatalogValueVO(DatalogDetailVO datum){
        if(CollectionUtil.isNotEmpty(datum.getDataList())){
            for (int i = datum.getDataList().size() -1 ; i >=0 ; i--) {
                DatalogValueVO datalogValueVO = datum.getDataList().get(i);
                if(null != datalogValueVO.getValue()){
                    return datalogValueVO;
                }
            }
        }

        return null;
    }

    /**
     * 集中式光伏历史数据处理
     * @param dto
     */
    private void dataLogForJzsgf(YznDataLogDTO dto) {
        //设备id集合
        List<Long> deviceIds = Collections.singletonList(12L);
        //参数id集合
        List<Long> paramIds = Arrays.asList(110041L,110021L);

        List<Map<String, String>> monthList = getMonthList();

        Map<String, SysParamSimulate> simulateMap = sysParamSimulateMapper.selectList(new LambdaQueryWrapper<SysParamSimulate>()
                .in(SysParamSimulate::getParamId,paramIds)
                .in(SysParamSimulate::getDeviceId,deviceIds)
        ).stream().collect(Collectors.toMap(t -> t.getDeviceId() + "" + t.getParamId(), t -> t));

        List<DataDeviceMonth> monthDataList = new ArrayList<>();

        for (Map<String, String> map : monthList) {
            String startTime = map.get("startTime");

            for (Long deviceId : deviceIds) {
                for (Long paramId : paramIds) {
                    SysParamSimulate sysParamSimulate = simulateMap.get(deviceId + "" + paramId);

                    DataDeviceMonth dataDeviceMonth = new DataDeviceMonth();
                    dataDeviceMonth.setId(SnowIdUtil.nextId());
                    dataDeviceMonth.setDeviceId(deviceId);
                    dataDeviceMonth.setParamId(paramId);
                    dataDeviceMonth.setParamValue(CustomUtils.getRandomDouble(
                            sysParamSimulate.getParamLowerLimit(),
                            sysParamSimulate.getParamUpperLimit(),
                            sysParamSimulate.getParamBaseValue()
                    ));
                    dataDeviceMonth.setParamTime(DateUtil.parse(startTime,"yyyy-MM"));
                    monthDataList.add(dataDeviceMonth);

                    if(dataDeviceMonth.getParamId().equals(110041L)){
                        //根据月发电量计算月收益（万元）
                        DataDeviceMonth dataDeviceMonth2 = new DataDeviceMonth();
                        dataDeviceMonth2.setId(SnowIdUtil.nextId());
                        dataDeviceMonth2.setDeviceId(deviceId);
                        dataDeviceMonth2.setParamId(100003L);
                        dataDeviceMonth2.setParamValue(dataDeviceMonth.getParamValue()*syFactor/10000);
                        dataDeviceMonth2.setParamTime(DateUtil.parse(startTime,"yyyy-MM"));
                        monthDataList.add(dataDeviceMonth2);

                        //根据月发电量计算上网
                        DataDeviceMonth dataDeviceMonth3 = new DataDeviceMonth();
                        dataDeviceMonth3.setId(SnowIdUtil.nextId());
                        dataDeviceMonth3.setDeviceId(deviceId);
                        dataDeviceMonth3.setParamId(100008L);
                        dataDeviceMonth3.setParamValue(dataDeviceMonth.getParamValue()*swFactor);
                        dataDeviceMonth3.setParamTime(DateUtil.parse(startTime,"yyyy-MM"));
                        monthDataList.add(dataDeviceMonth3);
                    }
                }
            }
        }

        if(CollectionUtil.isNotEmpty(monthDataList)){
            sysParamSimulateMapper.monthBatchInsertOrUpdate(monthDataList);
        }
    }

    @Override
    public void realTimeData(RealTimeDataDTO dto) {
        setFactor();
        if(xnyConfig.getUseSimulateData()){
            //开发环境全部使用模拟数据
            //dto.setIfDev(true);
            dto.setDataSourceList(Arrays.asList(1,4));
            List<DataSyncVO> dataSyncVOS = sysParamSimulateMapper.listRealTimeDataInfo(dto);
            RealtimeDataVO send = realTimeDataSimulation(dataSyncVOS,null);
            saveRealTimeData(send,true);
        }else {
            //风电、水电、集中式光伏 使用模拟数据
            //dto.setMeasureIdIfNull(true);
            dto.setDataSourceList(Collections.singletonList(4));
            List<DataSyncVO> dataSyncVOS2 = sysParamSimulateMapper.listRealTimeDataInfo(dto);
            RealtimeDataVO send2 = realTimeDataSimulation(dataSyncVOS2,null);
            saveRealTimeData(send2,true);


            //分布式光伏、充电项目、储能项目 使用云综能数据
            //dto.setMeasureIdIfNull(false);
            dto.setDataSourceList(Collections.singletonList(1));
            List<DataSyncVO> dataSyncVOS = sysParamSimulateMapper.listRealTimeDataInfo(dto);
            Map<Integer, List<DataSyncVO>> collect = dataSyncVOS.stream().collect(Collectors.groupingBy(DataSyncVO::getDeviceId));
            Set<Map.Entry<Integer, List<DataSyncVO>>> entries = collect.entrySet();

            for (Map.Entry<Integer, List<DataSyncVO>> entry : entries) {
                //ThreadUtil.execAsync(()->{
                    List<RealtimeDataDTO> list= new ArrayList<>();
                    for (DataSyncVO dataSyncVO : entry.getValue()) {
                        RealtimeDataDTO realtimeDataDTO = new RealtimeDataDTO();
                        realtimeDataDTO.setChannelId(dataSyncVO.getYznChannelId());
                        realtimeDataDTO.setDeviceId(dataSyncVO.getYznDeviceId());
                        realtimeDataDTO.setMeasureId(dataSyncVO.getYznMeasureId());
                        realtimeDataDTO.setStationId(dataSyncVO.getYznStationId());
                        list.add(realtimeDataDTO);
                    }
                    RealtimeDataVO send = RequestUtils.doSend(list, RealtimeDataVO.class);

                    Map<Integer, Integer> paramIdMap = entry.getValue().stream().collect(Collectors.toMap(DataSyncVO::getYznMeasureId, DataSyncVO::getParamId));
                    Map<Integer, Double> normalValueMap = entry.getValue().stream().filter(temp->BeanUtil.isNotEmpty(temp.getParamNormalValue())).collect(Collectors.toMap(DataSyncVO::getYznMeasureId, DataSyncVO::getParamNormalValue));
                    for (RealtimeDetailVO datum : send.getData()) {
                        datum.setSysParamId(paramIdMap.get(datum.getMeasureId()));
                        datum.setSysDeviceId(entry.getValue().get(0).getDeviceId());
                        Double aDouble = normalValueMap.get(datum.getMeasureId());
                        if(BeanUtil.isNotEmpty(aDouble)){
                            datum.setSysNormalValue(aDouble);
                        }
                    }
                    saveRealTimeData(send,false);
               // });
            }

        }

    }

    /**
     * 补全数据
     * @param compleDataDTO
     */
    public void compleData(CompleDataDTO compleDataDTO) {
        if(xnyConfig.getUseSimulateData()){
            return;
        }

        if(DateUtil.compare(compleDataDTO.getStartTime(),compleDataDTO.getEndTime())>0){
            throw new RuntimeException("开始时间不能大于结束时间");
        }

        setFactor();

        //分布式光伏、充电项目、储能项目 使用云综能数据
        RealTimeDataDTO dto = new RealTimeDataDTO();
        dto.setYznParamIdIfNull(false);
        dto.setDataSourceList(Collections.singletonList(1));
        //dto.setMeasureIdIfNull(false);
        List<DataSyncVO> dataSyncVOS = sysParamSimulateMapper.listRealTimeDataInfo(dto);

        Map<Integer, List<DataSyncVO>> map = dataSyncVOS.stream().collect(Collectors.groupingBy(DataSyncVO::getDeviceId));

        Map<Long, Long> paramMap = sysParamService.list().stream().collect(Collectors.toMap(SysParam::getYznParamId, SysParam::getId));
        Map<Long, Long> deviceMap = sysDeviceService.list().stream().collect(Collectors.toMap(SysDevice::getYznDeviceId, SysDevice::getId));

        DatalogDTO datalogDTO = new DatalogDTO();
        datalogDTO.setStartTime(DateUtil.format(compleDataDTO.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
        datalogDTO.setEndTime(DateUtil.format(compleDataDTO.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        datalogDTO.setInterval(5);

        Set<Map.Entry<Integer, List<DataSyncVO>>> entries = map.entrySet();

        for (Map.Entry<Integer, List<DataSyncVO>> entry : entries) {
            List<RealtimeDetailVO> dataList = new ArrayList<>();
            List<MeterConfigs> meterConfigList = new ArrayList<>();

            for (DataSyncVO dataSyncVO : entry.getValue()) {
                MeterConfigs meterConfigs = new MeterConfigs();
                meterConfigs.setDataId(dataSyncVO.getYznParamId());//参数ID
                meterConfigs.setDataTypeId(1);
                meterConfigs.setDeviceId(dataSyncVO.getYznDeviceId());//设备ID
                meterConfigs.setLogicalId(1);
                meterConfigList.add(meterConfigs);
            }
            datalogDTO.setMeterConfigs(meterConfigList);
            DatalogVO send = RequestUtils.doSend(datalogDTO, DatalogVO.class);

            if(send.getCode()==0 && CollectionUtil.isNotEmpty(send.getData())){
                List<DatalogDetailVO> data = send.getData();
                for (DatalogDetailVO datum : data) {
                    if(CollectionUtil.isNotEmpty(datum.getDataList())){
                        for (DatalogValueVO datalogValueVO : datum.getDataList()) {
                            RealtimeDetailVO realtimeDetailVO = new RealtimeDetailVO();
                            realtimeDetailVO.setValue(datalogValueVO.getValue());
                            realtimeDetailVO.setSysParamId(paramMap.get(Long.valueOf(datum.getDataId())).intValue());
                            realtimeDetailVO.setSysDeviceId(deviceMap.get(Long.valueOf(datum.getDeviceId())).intValue());
                            realtimeDetailVO.setSysDate(DateUtil.parse(datalogValueVO.getTimeStr(),"yyyy-MM-dd HH:mm:ss"));
                            dataList.add(realtimeDetailVO);
                        }
                    }
                }
            }

            if(CollectionUtil.isNotEmpty(dataList)){
                RealtimeDataVO realtimeDataVO = new RealtimeDataVO();
                realtimeDataVO.setData(dataList);
                saveRealTimeData(realtimeDataVO,true);
            }
        }


        //风电、水电、集中式光伏 使用模拟数据
        dto.setYznParamIdIfNull(null);
        dto.setDataSourceList(Collections.singletonList(4));
        //dto.setMeasureIdIfNull(true);
        List<DataSyncVO> dataSyncVOS2 = sysParamSimulateMapper.listRealTimeDataInfo(dto);

        List<RealtimeDetailVO> dataList2 = new ArrayList<>();
        while(DateUtil.compare(compleDataDTO.getStartTime(),compleDataDTO.getEndTime())<=0){
            RealtimeDataVO send2 = realTimeDataSimulation(dataSyncVOS2,compleDataDTO.getStartTime());
            if(CollectionUtil.isNotEmpty(send2.getData())){
                dataList2.addAll(send2.getData());
            }
            compleDataDTO.setStartTime(DateUtil.offsetMinute(compleDataDTO.getStartTime(),5));
        }

        if(CollectionUtil.isNotEmpty(dataList2)){
            RealtimeDataVO realtimeDataVO = new RealtimeDataVO();
            realtimeDataVO.setData(dataList2);
            saveRealTimeData(realtimeDataVO,true);
        }

    }





    /**
     * 实时数据保存-指定时间
     * @param send
     */
    private void saveRealTimeData(RealtimeDataVO send,boolean ifSimulationData) {
        if(send.getCode()==0){
            List<DataDeviceRealTime> realTimeDataList = new ArrayList<>();
            int alarmCount = 0;
            List<DataDeviceHour> hourDataList = new ArrayList<>();
            List<DataDeviceMonth> monthDataList = new ArrayList<>();
            List<DataParamHistory> historyDataList = new ArrayList<>();

            Date nowDate = new Date();

            for (RealtimeDetailVO datum : send.getData()) {
                if(null == datum.getValue() || BigDecimal.ZERO.compareTo(BigDecimal.valueOf(datum.getValue()))==0){
                    continue;
                }
                //记录实时值
                DataDeviceRealTime dataDeviceRealTime = new DataDeviceRealTime();
                dataDeviceRealTime.setId(SnowIdUtil.nextId());
                dataDeviceRealTime.setDeviceId(datum.getSysDeviceId().longValue());
                dataDeviceRealTime.setParamId(datum.getSysParamId().longValue());
                dataDeviceRealTime.setParamValue(null == datum.getValue() ? 0 : datum.getValue());
                dataDeviceRealTime.setParamTime(null == datum.getSysDate() ? nowDate : datum.getSysDate());
                if(!alarmCountList.contains(dataDeviceRealTime.getParamId()) && null != datum.getValue()){
                    realTimeDataList.add(dataDeviceRealTime);
                }

                //记录实时值到历史数据表
                DataParamHistory dataParamHistory = new DataParamHistory();
                dataParamHistory.setId(SnowIdUtil.nextId());
                dataParamHistory.setDeviceId(datum.getSysDeviceId().longValue());
                dataParamHistory.setParamId(datum.getSysParamId().longValue());
                dataParamHistory.setParamValue(null == datum.getValue() ? 0 : datum.getValue());
                dataParamHistory.setParamTime(nowDate);
                historyDataList.add(dataParamHistory);

                //计算收益
                if(calculateSyList.contains(dataDeviceRealTime.getParamId())){
                    calculateSy(dataDeviceRealTime,realTimeDataList);
                }

                //记录小时值
                if(recordHourList.contains(dataDeviceRealTime.getParamId())){
                    recordHour(dataDeviceRealTime,hourDataList);
                }

                //记录月数据
                if(recordMonthList.contains(dataDeviceRealTime.getParamId())){
                    recordMonth(dataDeviceRealTime,monthDataList);
                }

                //记录储能设备告警次数
                if(alarmList.contains(dataDeviceRealTime.getParamId())){
                    DataDeviceRealTime lastOne = dataDeviceRealTimeService.getOne(new LambdaQueryWrapper<DataDeviceRealTime>()
                            .eq(DataDeviceRealTime::getParamId, dataDeviceRealTime.getParamId())
                            .eq(DataDeviceRealTime::getDeviceId, dataDeviceRealTime.getDeviceId())
                    );

                    if(datum.getSysNormalValue().compareTo(lastOne.getParamValue()) == 0 &&
                            datum.getSysNormalValue().compareTo(dataDeviceRealTime.getParamValue()) != 0){
                        alarmCount++;
                    }
                }
            }

            if(CollectionUtil.isNotEmpty(realTimeDataList)){
                sysParamSimulateMapper.realTimeBatchInsertOrUpdate(realTimeDataList);

                if(alarmCount != 0){
                    this.updateAlarmCount(alarmCount);
                }
            }

            if(CollectionUtil.isNotEmpty(hourDataList)){
                sysParamSimulateMapper.hourBatchInsertOrUpdate(hourDataList);
            }

            if(CollectionUtil.isNotEmpty(monthDataList)){
                sysParamSimulateMapper.monthBatchInsertOrUpdate(monthDataList);
            }

            if(CollectionUtil.isNotEmpty(historyDataList) && !ifSimulationData){
                ThreadUtil.execAsync(()->
                    sysParamSimulateMapper.historyBatchInsertOrUpdate(historyDataList)
                );
            }
        }
    }

    /**
     * 更新储能设备告警次数
     * @param alarmCount
     */
    private void updateAlarmCount(Integer alarmCount) {
        DataDeviceRealTime one = dataDeviceRealTimeService.getOne(new LambdaQueryWrapper<DataDeviceRealTime>()
                .eq(DataDeviceRealTime::getParamId,100023L)
                .eq(DataDeviceRealTime::getDeviceId, 8L)
        );

        if(BeanUtil.isEmpty(one)){
            dataDeviceRealTimeService.save(new DataDeviceRealTime()
                    .setId(SnowIdUtil.nextId())
                    .setDeviceId(8L)
                    .setParamId(100023L)
                    .setParamValue(Double.valueOf(alarmCount))
                    .setParamTime(new Date())
                    .setCreateTime(new Date()));

            dataDeviceRealTimeService.save(new DataDeviceRealTime()
                    .setId(SnowIdUtil.nextId())
                    .setDeviceId(8L)
                    .setParamId(100024L)
                    .setParamValue(Double.valueOf(alarmCount))
                    .setParamTime(new Date())
                    .setCreateTime(new Date()));
        }else {
            sysParamSimulateMapper.updateAlarmCount(new UpdateAlarmCountDTO()
                    .setAlramCount(alarmCount)
                    .setParamId(100023L)
                    .setDeviceId(8L)
                    .setType(DateUtil.compare(one.getParamTime(),DateUtil.beginOfDay(new Date())) >=0 ? 2:1)
            );
            sysParamSimulateMapper.updateAlarmCount(new UpdateAlarmCountDTO()
                    .setAlramCount(alarmCount)
                    .setParamId(100024L)
                    .setDeviceId(8L)
                    .setType(2)
            );
        }
    }

    /**
     * 记录 小时 值
     * @param data
     * @param list
     */
    private void recordHour(DataDeviceRealTime data,List<DataDeviceHour> list){
        String format = DateUtil.format(data.getParamTime(), "yyyy-MM-dd HH:mm:ss");
        DataDeviceHour dataDeviceHour = BeanUtil.copyProperties(data, DataDeviceHour.class);
        dataDeviceHour.setParamTime(DateUtil.parse(format,"yyyy-MM-dd HH"));
        list.add(dataDeviceHour);
    }

    /**
     * 记录 月 值
     * @param data
     * @param list
     */
    private void recordMonth(DataDeviceRealTime data,List<DataDeviceMonth> list){
        String format = DateUtil.format(data.getParamTime(), "yyyy-MM-dd HH:mm:ss");
        DataDeviceMonth dataDeviceMonth = BeanUtil.copyProperties(data, DataDeviceMonth.class);
        dataDeviceMonth.setParamTime(DateUtil.parse(format,"yyyy-MM"));
        list.add(dataDeviceMonth);

        if(data.getParamId().equals(110041L)){
            DataDeviceMonth dataDeviceMonth2 = BeanUtil.copyProperties(data, DataDeviceMonth.class);
            dataDeviceMonth2.setParamTime(DateUtil.parse(format,"yyyy-MM"));
            dataDeviceMonth2.setParamId(100008L);
            dataDeviceMonth2.setParamValue(data.getParamValue()*swFactor);
            list.add(dataDeviceMonth2);

            DataDeviceMonth dataDeviceMonth3 = BeanUtil.copyProperties(data, DataDeviceMonth.class);
            dataDeviceMonth3.setParamTime(DateUtil.parse(format,"yyyy-MM"));
            dataDeviceMonth3.setParamId(100003L);
            dataDeviceMonth3.setParamValue(data.getParamValue()*syFactor/10000);
            list.add(dataDeviceMonth3);
        }
    }


    /**
     * 计算收益
     * @param data
     * @param list
     */
    private void calculateSy(DataDeviceRealTime data,List<DataDeviceRealTime> list){
        if(!deviceIdList.contains(data.getDeviceId())){
            return;
        }

        DataDeviceRealTime sy = BeanUtil.copyProperties(data, DataDeviceRealTime.class);

        if(data.getParamId().equals(110003L)){
            sy.setParamId(100002L).setParamValue(data.getParamValue()*syFactor);
            list.add(sy);
        }
        else if(data.getParamId().equals(110041L)){
            sy.setParamId(100003L).setParamValue(data.getParamValue()*syFactor/10000);
            list.add(sy);
        }
        else if(data.getParamId().equals(110004L)){
            DataDeviceRealTime zsw = BeanUtil.copyProperties(data, DataDeviceRealTime.class);
            DataDeviceRealTime jydf = BeanUtil.copyProperties(data, DataDeviceRealTime.class);

            sy.setParamId(100005L).setParamValue(data.getParamValue()*syFactor/10000);//总收益
            zsw.setParamId(100008L).setParamValue(data.getParamValue()*swFactor);//总上网
            jydf.setParamId(100011L).setParamValue(sy.getParamValue());//节约电费

            list.add(sy);
            list.add(zsw);
            list.add(jydf);
        }
    }



    /**
     * 实时数据模拟
     * @param list
     * @return
     */
    private RealtimeDataVO realTimeDataSimulation(List<DataSyncVO> list,Date date) {
        List<RealtimeDetailVO> dataList = new ArrayList<>();

        for (DataSyncVO dataSyncVO : list) {
            RealtimeDetailVO realtimeDetailVO = new RealtimeDetailVO();
            realtimeDetailVO.setStationId(dataSyncVO.getYznStationId());
            realtimeDetailVO.setChannelId(dataSyncVO.getYznChannelId());
            realtimeDetailVO.setDeviceId(dataSyncVO.getYznDeviceId());
            realtimeDetailVO.setMeasureId(dataSyncVO.getYznMeasureId());
            realtimeDetailVO.setSysDeviceId(dataSyncVO.getDeviceId());
            realtimeDetailVO.setSysParamId(dataSyncVO.getParamId());
            realtimeDetailVO.setSysNormalValue(dataSyncVO.getParamNormalValue());
            realtimeDetailVO.setValue(CustomUtils.getRandomDouble(
                    dataSyncVO.getParamLowerLimit(),
                    dataSyncVO.getParamUpperLimit(),
                    dataSyncVO.getParamBaseValue()
            ));
            realtimeDetailVO.setSysDate(date);
            dataList.add(realtimeDetailVO);
        }

        RealtimeDataVO realtimeDataVO = new RealtimeDataVO();
        realtimeDataVO.setData(dataList);
        realtimeDataVO.setCode(0);
        realtimeDataVO.setMsg("");
        realtimeDataVO.setSuccess(true);
        realtimeDataVO.setTotal(dataList.size());
        return realtimeDataVO;
    }

    /**
     * 历史数据模拟
     * @param datalogDTO
     * @return
     */
    private DatalogVO dataLogSimulation(DatalogDTO datalogDTO,Map<String, SysParamSimulate> simulateMap){
        DatalogVO datalogVO = new DatalogVO();
        datalogVO.setData(Lists.newArrayList());
        datalogVO.setCode(0);
        datalogVO.setMsg("");
        datalogVO.setSuccess(true);
        datalogVO.setTotal(0);

        List<DatalogDetailVO> dataList = new ArrayList<>();
        List<MeterConfigs> meterConfigs = datalogDTO.getMeterConfigs();
        for (MeterConfigs meterConfig : meterConfigs) {
            DatalogDetailVO datalogDetailVO = new DatalogDetailVO();
            datalogDetailVO.setDataId(meterConfig.getDataId());
            datalogDetailVO.setDataTypeId(meterConfig.getDataTypeId());
            datalogDetailVO.setDeviceId(meterConfig.getDeviceId());
            datalogDetailVO.setLogicalId(meterConfig.getLogicalId());

            SysParamSimulate sysParamSimulate = simulateMap.get(meterConfig.getDeviceId() + "" + meterConfig.getDataId());

            List<DatalogValueVO> list = new ArrayList<>();
            DatalogValueVO datalogValueVO = new DatalogValueVO();
            datalogValueVO.setTimeStr(datalogDTO.getStartTime());
            datalogValueVO.setValue(CustomUtils.getRandomDouble(
                    sysParamSimulate.getParamLowerLimit(),
                    sysParamSimulate.getParamUpperLimit(),
                    sysParamSimulate.getParamBaseValue()
            ));
            list.add(datalogValueVO);
            datalogDetailVO.setDataList(list);
            dataList.add(datalogDetailVO);
        }

        datalogVO.setData(dataList);

        return datalogVO;
    }

    /**
     * 获取前 12个月 月末 日期
     * @return
     */
    private List<Map<String,String>> getMonthList(){
        DateTime dateTime = DateUtil.endOfMonth(new Date());
        DateTime dateTime1 = DateUtil.offsetMonth(dateTime, -1);
        DateTime dateTime2 = DateUtil.offsetMonth(dateTime1, -11);

        List<DateTime> dateTimes = DateUtil.rangeToList(dateTime2, dateTime1, DateField.MONTH);

        List<Map<String,String>> resultList = new ArrayList<>();
        for (DateTime time : dateTimes) {
            Map<String,String> map = new HashMap<>();
            DateTime dateTime3 = DateUtil.endOfMonth(time);
            map.put("startTime",DateUtil.format(DateUtil.beginOfDay(dateTime3),"yyyy-MM-dd HH:mm:ss"));
            map.put("endTime",DateUtil.format(dateTime3, "yyyy-MM-dd HH:mm:ss"));
            resultList.add(map);
        }
        return resultList ;
    }

    /**
     * 获取参数列表
     * @return
     */
    private List<MeterConfigDTO> getParamList(List<Long> deviceIds,List<Long> paramIds){
        List<MeterConfigDTO> resultList = new ArrayList<>();
        for (Long deviceId : deviceIds) {
            for (Long paramId : paramIds) {
                MeterConfigDTO meterConfigDTO = new MeterConfigDTO();
                meterConfigDTO.setParamId(paramId);
                meterConfigDTO.setDeviceId(deviceId);
                resultList.add(meterConfigDTO);
            }
        }
        return resultList;
    }

    //云综能设备id 和 系统设备id 匹配关系
    private Map<Integer,Long> getTempMap(){
        Map<Integer,Long> map = new HashMap<>();
        map.put(30,1L);
        map.put(31,2L);
        map.put(32,3L);
        map.put(33,4L);
        map.put(34,5L);
        map.put(35,6L);
        map.put(36,7L);
        map.put(50,13L);
        map.put(6000254,110041L);
        map.put(2000004,110021L);
        return map;
    }
}
