package com.xny.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xny.common.utils.SnowIdUtil;
import com.xny.system.entity.DataDeviceHour;
import com.xny.system.entity.DataDeviceRealTime;
import com.xny.system.entity.DataQdWarn;
import com.xny.system.mapper.SysParamSimulateMapper;
import com.xny.system.model.vo.GetWarningsVO;
import com.xny.system.model.vo.GeteBlockTotalVO;
import com.xny.system.model.vo.GeteLinkTotalVO;
import com.xny.system.model.vo.GeteMeterTotalVO;
import com.xny.system.qd.model.vo.DevicetransientStatusVO;
import com.xny.system.qd.model.vo.EnergyCapabilityVO;
import com.xny.system.qd.model.vo.MainTotalVO;
import com.xny.system.qd.utils.QdUtils;
import com.xny.system.service.IDataQdWarnService;
import com.xny.system.service.QdService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class QdServiceImpl implements QdService {
    @Resource
    private SysParamSimulateMapper sysParamSimulateMapper;

    @Resource
    private IDataQdWarnService dataQdWarnService;

    @Override
    public void getQdData() {
        GeteBlockTotalVO blockTotalVO = QdUtils.geteBlockTotal().get(0);
        GeteLinkTotalVO linkTotalVO = QdUtils.geteLinkTotal().get(0);
        GetWarningsVO warnings = QdUtils.getWarnings();
        DevicetransientStatusVO devicetransientStatus = QdUtils.getDevicetransientStatus();
        EnergyCapabilityVO energyCapability = QdUtils.getEnergyCapability();

        Date date = new Date();
        DateTime parse = DateUtil.parse(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH");
        Long deviceId = 8L;


        //异常信息
        if(CollectionUtil.isNotEmpty(warnings.getOfflineAlert())){
            List<DataQdWarn> dataQdWarns = CglibUtil.copyList(warnings.getOfflineAlert(), DataQdWarn::new);
            dataQdWarnService.saveOrUpdateBatch(dataQdWarns);
        }

        if(CollectionUtil.isNotEmpty(warnings.getUnplannedOperation())){
            List<DataQdWarn> dataQdWarns = CglibUtil.copyList(warnings.getUnplannedOperation(), DataQdWarn::new);
            dataQdWarnService.saveOrUpdateBatch(dataQdWarns);
        }

        long count = dataQdWarnService.count(new LambdaQueryWrapper<DataQdWarn>()
                .eq(DataQdWarn::getDelFlag, 0)
                .gt(DataQdWarn::getStartTime, DateUtil.beginOfDay(date))
                .le(DataQdWarn::getEndTime, DateUtil.endOfDay(date))
        );

        //120077L,120075L,120076L,120072L,120073L
        //"直流母线功率","直流母线电压","直流母线电流","储能系统SOC","储能系统SOH"

        //120002L,120014L,120015L,120026L,120025L,
        //120080L,120083L,120086L,120089L,120092L
        //"储能有功功率","方阵额定功率","方阵额定容量","剩余可放电量","剩余可充电量",
        //"柜体温度","最高电压值","最低电压值","最高温度","最低温度"


        //记录实时值
        List<DataDeviceRealTime> realTimeDataList = new ArrayList<>(Arrays.asList(
                new DataDeviceRealTime().setParamId(120077L).setParamValue(Double.valueOf(blockTotalVO.getP_dc())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120075L).setParamTime(date).setParamValue(Double.valueOf(blockTotalVO.getU_dc())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120076L).setParamTime(date).setParamValue(Double.parseDouble(blockTotalVO.getI_dc())*10).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120072L).setParamTime(date).setParamValue(Double.valueOf(blockTotalVO.getSoc())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120073L).setParamTime(date).setParamValue(Double.valueOf(blockTotalVO.getSoh())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),

                new DataDeviceRealTime().setParamId(120002L).setParamTime(date).setParamValue(Double.valueOf(linkTotalVO.getP_total())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120014L).setParamTime(date).setParamValue(Double.valueOf(devicetransientStatus.getRated_Power())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120015L).setParamTime(date).setParamValue(Double.valueOf(devicetransientStatus.getRated_Capacity())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120026L).setParamTime(date).setParamValue(Double.valueOf(energyCapability.getSum_e_dischargeable())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120025L).setParamTime(date).setParamValue(Double.valueOf(energyCapability.getSum_e_chargeable())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120080L).setParamTime(date).setParamValue(Double.valueOf(blockTotalVO.getTemp_cabinet())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120083L).setParamTime(date).setParamValue(Double.parseDouble(blockTotalVO.getU_cell_max())/1000d).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120086L).setParamTime(date).setParamValue(Double.parseDouble(blockTotalVO.getU_cell_min())/1000d).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120089L).setParamTime(date).setParamValue(Double.valueOf(blockTotalVO.getTemp_cell_max())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),
                new DataDeviceRealTime().setParamId(120092L).setParamTime(date).setParamValue(Double.valueOf(blockTotalVO.getTemp_cell_min())).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date),

                //今日告警数量
                new DataDeviceRealTime().setParamId(100023L).setParamTime(date).setParamValue((double) count).setId(SnowIdUtil.nextId()).setDeviceId(deviceId).setParamTime(date)

        ));

        if(CollectionUtil.isNotEmpty(realTimeDataList)){
            sysParamSimulateMapper.realTimeBatchInsertOrUpdate(realTimeDataList);
        }

    }

}
