package com.xny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.RedisUtils;
import com.xny.common.utils.SnowIdUtil;
import com.xny.system.entity.*;
import com.xny.system.mapper.DataYkcEquipmentMapper;
import com.xny.system.mapper.SysParamSimulateMapper;
import com.xny.system.model.vo.YkcEquipmentVO;
import com.xny.system.service.IDataYkcEquipmentService;
import com.xny.system.service.IDataYkcStationService;
import com.xny.system.service.YkcService;
import com.xny.system.ykc.config.YkcConfig;
import com.xny.system.ykc.model.dto.*;
import com.xny.system.ykc.model.vo.StationInfo;
import com.xny.system.ykc.model.vo.StationInfos;
import com.xny.system.ykc.model.vo.StationStatusInfo;
import com.xny.system.ykc.model.vo.StationStatusInfos;
import com.xny.system.ykc.utils.YkcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YkcServiceImpl implements YkcService {
    @Resource
    private YkcConfig ykcConfig;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private IDataYkcStationService ykcStationService;

    @Resource
    private IDataYkcEquipmentService ykcEquipmentService;

    @Resource
    private SysParamSimulateMapper sysParamSimulateMapper;

    @Resource
    private DataYkcEquipmentMapper dataYkcEquipmentMapper;

    @Override
    public JSONObject queryToken(JSONObject obj) {
        String queryToken = YkcUtils.pushVerifyAndDecrypt(obj, "queryToken");
        TokenDTO pushTokenDTO = JSONUtil.toBean(queryToken, TokenDTO.class);
        if(!pushTokenDTO.getOperatorID().equals(ykcConfig.getYkcOperatorID())){
            JSONObject data = new JSONObject();
            data.putOnce("SuccStat",1);
            data.putOnce("FailReason",1);
            return YkcUtils.getPushResponse(data);
        }
        if(!pushTokenDTO.getOperatorSecret().equals(ykcConfig.getZnOperatorSecret())){
            JSONObject data = new JSONObject();
            data.putOnce("SuccStat",1);
            data.putOnce("FailReason",2);
            return YkcUtils.getPushResponse(data);
        }

        String token = IdUtil.fastSimpleUUID();
        redisUtils.set("Bearer "+token,"1",432000L);

        JSONObject data = new JSONObject();
        data.putOnce("OperatorID",pushTokenDTO.getOperatorID());
        data.putOnce("SuccStat",0);
        data.putOnce("AccessToken", token);
        data.putOnce("TokenAvailableTime",432000);
        data.putOnce("FailReason",0);
        return YkcUtils.getPushResponse(data);
    }

    @Override
    public JSONObject stationStatus(JSONObject obj) {
        String stationStatus = YkcUtils.pushVerifyAndDecrypt(obj, "stationStatus");
        StationStatusDTO stationStatusDTO = JSONUtil.toBean(stationStatus, StationStatusDTO.class);

        if(BeanUtil.isNotEmpty(stationStatusDTO) && BeanUtil.isNotEmpty(stationStatusDTO.getConnectorStatusInfo())){
            setEquipmentData(stationStatusDTO.getConnectorStatusInfo());
        }

        JSONObject data = new JSONObject();
        data.putOnce("Status",0);
        return YkcUtils.getPushResponse(data);
    }

    /**
     * 设置设备数据
     * @param connectorStatusInfo
     */
    private void setEquipmentData(ConnectorStatusInfo connectorStatusInfo) {
        DataYkcEquipment dataYkcEquipment = new DataYkcEquipment();
        dataYkcEquipment.setConnectorID(connectorStatusInfo.getConnectorID());
        dataYkcEquipment.setStationId(connectorStatusInfo.getStationID());
        Integer status = connectorStatusInfo.getStatus();
        if(status==0 || status==255){
            dataYkcEquipment.setStatus(2);
        }else if(status==2 || status==3 || status==4){
            dataYkcEquipment.setStatus(1);
        }else {
            dataYkcEquipment.setStatus(0);
        }
        ykcEquipmentService.saveOrUpdate(dataYkcEquipment);

        List<YkcEquipmentVO> ykcEquipmentVOS = dataYkcEquipmentMapper.ykcEquipmentList();
        long totalCount = ykcEquipmentVOS.size();
        long inUseCount = ykcEquipmentVOS.stream().filter(s -> s.getStatus()==1).count();
        long freeCount = ykcEquipmentVOS.stream().filter(s -> s.getStatus()==0).count();
        long alarmCount = ykcEquipmentVOS.stream().filter(s -> s.getStatus()==2).count();

        //130051使用中 130052空闲中 100023今日告警 130054充电桩在线率 130055充电桩使用率 130056充电桩良好率 130057充电桩故障率
        Date date = new Date();
        double useRate = BigDecimal.valueOf(inUseCount + freeCount).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP).doubleValue();
        List<DataDeviceRealTime> list = Arrays.asList(
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(130051L).setParamTime(date).setParamValue((double) inUseCount),
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(130052L).setParamTime(date).setParamValue((double) freeCount),
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(100023L).setParamTime(date).setParamValue((double) alarmCount),
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(130054L).setParamTime(date).setParamValue(useRate),
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(130055L).setParamTime(date).setParamValue(BigDecimal.valueOf(inUseCount)
                        .multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP).doubleValue()),
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(130056L).setParamTime(date).setParamValue(useRate),
                new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                        .setParamId(130057L).setParamTime(date).setParamValue(BigDecimal.valueOf(alarmCount)
                        .multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP).doubleValue())
        );
        sysParamSimulateMapper.realTimeBatchInsertOrUpdate(list);
    }

    @Override
    public JSONObject chargeStatus(JSONObject obj) {
        String chargeStatus = YkcUtils.pushVerifyAndDecrypt(obj, "chargeStatus");
        ChargeStatusDTO chargeStatusDTO = JSONUtil.toBean(chargeStatus, ChargeStatusDTO.class);

        if(BeanUtil.isNotEmpty(chargeStatusDTO)){
            //130059	充电电流 130060	充电电压 130061	充电功率
            String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            DateTime parse = DateUtil.parse(format, "yyyy-MM-dd HH");
            List<DataDeviceHour> list = Arrays.asList(
                    new DataDeviceHour().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(130059L).setParamTime(parse).setParamValue((double)chargeStatusDTO.getCurrentA()),
                    new DataDeviceHour().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(130060L).setParamTime(parse).setParamValue((double)chargeStatusDTO.getVoltageA()),
                    new DataDeviceHour().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(130061L).setParamTime(parse).setParamValue((double)chargeStatusDTO.getCurrentA()*(double)chargeStatusDTO.getVoltageA())
            );
            sysParamSimulateMapper.hourBatchInsertOrUpdate(list);

            //data_ykc_equipment 表数据更新
            DataYkcEquipment dataYkcEquipment = new DataYkcEquipment();
            dataYkcEquipment.setCurrentA(BigDecimal.valueOf(chargeStatusDTO.getCurrentA()));
            dataYkcEquipment.setVoltageA(BigDecimal.valueOf(chargeStatusDTO.getVoltageA()));
            dataYkcEquipment.setStartTime(chargeStatusDTO.getStartTime());
            dataYkcEquipment.setEndTime(chargeStatusDTO.getEndTime());
            dataYkcEquipment.setTotalMoney(BigDecimal.valueOf(chargeStatusDTO.getTotalMoney()));
            dataYkcEquipment.setConnectorID(chargeStatusDTO.getConnectorID());
            Integer status = chargeStatusDTO.getConnectorStatus();
            if(status==0 || status==255){
                dataYkcEquipment.setStatus(2);
            }else if(status==2 || status==3 || status==4){
                dataYkcEquipment.setStatus(1);
            }else {
                dataYkcEquipment.setStatus(0);
            }
            ykcEquipmentService.saveOrUpdate(dataYkcEquipment);
        }

        JSONObject data = new JSONObject();
        data.putOnce("Status",0);
        return YkcUtils.getPushResponse(data);
    }

    @Override
    public JSONObject chargeOrderInfo(JSONObject obj) {
        String chargeOrderInfoStatus = YkcUtils.pushVerifyAndDecrypt(obj, "chargeOrderInfo");
        ChargeOrderInfoDTO chargeOrderInfoStatusDTO = JSONUtil.toBean(chargeOrderInfoStatus, ChargeOrderInfoDTO.class);
        if(BeanUtil.isNotEmpty(chargeOrderInfoStatusDTO)){
            //100005总收益  120037总充电量 100006总充电时长 100007总充电次数 130053总用户数
            List<DataDeviceRealTime> list = Arrays.asList(
                    new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(100005L).setParamTime(new Date())
                            .setParamValue(chargeOrderInfoStatusDTO.getTotalMoney()),
                    new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(120037L).setParamTime(new Date()).setParamValue(chargeOrderInfoStatusDTO.getTotalPower()),
                    new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(100006L).setParamTime(new Date())
                            .setParamValue(BigDecimal.valueOf((chargeOrderInfoStatusDTO.getEndTime().getTime()-chargeOrderInfoStatusDTO.getStartTime().getTime()) /1000).divide(BigDecimal.valueOf(3600),3,RoundingMode.HALF_UP).doubleValue()),
                    new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(100007L).setParamTime(new Date()).setParamValue(1d),
                    new DataDeviceRealTime().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(130053L).setParamTime(new Date()).setParamValue(1d)
            );
            sysParamSimulateMapper.realTimeBatchInsertOrUpdateAccrual(list);

            //130058日充电量
            String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            List<DataDeviceDay> list2 = Collections.singletonList(
                    new DataDeviceDay().setId(SnowIdUtil.nextId()).setDeviceId(9L)
                            .setParamId(130058L).setParamTime(DateUtil.parse(format,"yyyy-MM-dd"))
                            .setParamValue(chargeOrderInfoStatusDTO.getTotalPower())
            );
            sysParamSimulateMapper.dayBatchInsertOrUpdateAccrual(list2);

        }

        JSONObject data = new JSONObject();
        data.putOnce("Status",0);
        return YkcUtils.getPushResponse(data);
    }

    @Override
    public void getYkcStationInfo() {
        String stations = YkcUtils.getStations();
        StationInfos stationInfos = JSONUtil.toBean(stations, StationInfos.class);
        if(BeanUtil.isEmpty(stationInfos) || CollectionUtil.isEmpty(stationInfos.getStationInfos())){
            log.info("未查询到充电站点信息");
            return;
        }

        List<DataYkcStation> list = new ArrayList<>();
        for (StationInfo stationInfo : stationInfos.getStationInfos()) {
            DataYkcStation dataYkcStation = new DataYkcStation();
            dataYkcStation.setStationId(stationInfo.getStationID());
            dataYkcStation.setStationName(stationInfo.getStationName());
            dataYkcStation.setOperatorId(stationInfo.getOperatorID());
            dataYkcStation.setEquipmentOwnerId(stationInfo.getEquipmentOwnerID());
            dataYkcStation.setCountryCode(stationInfo.getCountryCode());
            list.add(dataYkcStation);
        }
        ykcStationService.saveOrUpdateBatch(list);

        String stationsStatus = YkcUtils.getStationsStatus(list.stream().map(DataYkcStation::getStationId).collect(Collectors.toList()));
        StationStatusInfos stationStatusInfos = JSONUtil.toBean(stationsStatus, StationStatusInfos.class);
        if(BeanUtil.isEmpty(stationStatusInfos) || CollectionUtil.isEmpty(stationStatusInfos.getStationStatusInfos())){
            log.info("未查询到充电站点设备状态信息");
            return;
        }

        List<DataDeviceRealTime> realTimeList = new ArrayList<>();
        for (StationStatusInfo stationStatusInfo : stationStatusInfos.getStationStatusInfos()) {
            for (ConnectorStatusInfo connectorStatusInfo : stationStatusInfo.getConnectorStatusInfos()) {
                connectorStatusInfo.setStationID(stationStatusInfo.getStationID());
                setEquipmentData(connectorStatusInfo);
            }

            //充电桩总数
            if(CollectionUtil.isNotEmpty(stationStatusInfo.getConnectorStatusInfos())){
                DataDeviceRealTime dataDeviceRealTime = new DataDeviceRealTime();
                dataDeviceRealTime.setId(SnowIdUtil.nextId());
                dataDeviceRealTime.setDeviceId(9L);
                dataDeviceRealTime.setParamId(130050L);
                dataDeviceRealTime.setParamValue((double) stationStatusInfo.getConnectorStatusInfos().size());
                dataDeviceRealTime.setParamTime(new Date());
                realTimeList.add(dataDeviceRealTime);
            }
        }

        if(CollectionUtil.isNotEmpty(realTimeList)){
            sysParamSimulateMapper.realTimeBatchInsertOrUpdate(realTimeList);
        }
    }

}
