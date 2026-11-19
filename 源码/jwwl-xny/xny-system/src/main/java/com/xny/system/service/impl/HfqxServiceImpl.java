package com.xny.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xny.system.entity.DataHfqxEquipment;
import com.xny.system.hfqx.model.vo.*;
import com.xny.system.hfqx.utils.HfqxUtils;
import com.xny.system.service.HfqxService;
import com.xny.system.service.IDataHfqxEquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HfqxServiceImpl implements HfqxService {
    @Resource
    private IDataHfqxEquipmentService dataHfqxEquipmentService;

    /**
     * 同步设备信息
     */
    @Override
    public void syncHfEquipmentInfo() {
        DeviceInfoListVO deviceInfoList = HfqxUtils.getDeviceInfoList();

        if(CollectionUtil.isNotEmpty(deviceInfoList.getData())){
            List<DataHfqxEquipment> list = new ArrayList<>();
            for (HfEquipmentInfo datum : deviceInfoList.getData()) {
                DataHfqxEquipment dataHfqxEquipment = new DataHfqxEquipment();
                dataHfqxEquipment.setDeviceNumber(datum.getDeviceNumber());
                dataHfqxEquipment.setDeviceName(datum.getDeviceName());
                dataHfqxEquipment.setWorkMode(datum.getWorkStatus());
                dataHfqxEquipment.setOfflineStatus(datum.getIsOnline());
                if(datum.getIsOnline() == 0){
                    dataHfqxEquipment.setOfflineDays("小于3");
                }
                dataHfqxEquipment.setWorkStatus(datum.getLocationTime());
                list.add(dataHfqxEquipment);
            }
            dataHfqxEquipmentService.saveOrUpdateBatch(list);
        }
    }

    /**
     * 同步身设备状态位置信息
     */
    @Override
    public void syncHfEquipmentStatusInfo() {
        //同步设备实时位置信息
        DeviceLKLocationVO userDeviceLKLocation = HfqxUtils.getUserDeviceLKLocation();
        if(CollectionUtil.isNotEmpty(userDeviceLKLocation.getData())){
            List<DataHfqxEquipment> list = new ArrayList<>();
            for (HfRealTimeLocation datum : userDeviceLKLocation.getData()) {
                DataHfqxEquipment dataHfqxEquipment = new DataHfqxEquipment();
                dataHfqxEquipment.setDeviceNumber(datum.getDeviceNumber());
                dataHfqxEquipment.setOfflineStatus(datum.getIsOnline());
                if(datum.getIsOnline() == 0){
                    dataHfqxEquipment.setOfflineDays("小于3");
                }
                dataHfqxEquipment.setIsEnd(datum.getIsEnd());
                dataHfqxEquipment.setIsStart(datum.getIsStart());
                dataHfqxEquipment.setRealTimeAddress(datum.getAddress());
                dataHfqxEquipment.setStartTime(String.valueOf(datum.getStartTime()));
                dataHfqxEquipment.setEndTime(String.valueOf(datum.getEndTime()));
                list.add(dataHfqxEquipment);
            }
            dataHfqxEquipmentService.saveOrUpdateBatch(list);
        }

        //同步设备状态信息
        LongTimeOffLineVO longTimeOffLine = HfqxUtils.getLongTimeOffLine();
        LongTimeOffLineData data = longTimeOffLine.getData();
        if(CollectionUtil.isNotEmpty(data.getThreeList())){
            List<String> collect = data.getThreeList().stream().map(HfEquipmentInfo::getDeviceNumber).collect(Collectors.toList());
            dataHfqxEquipmentService.update(new DataHfqxEquipment().setOfflineDays("3")
                    ,new LambdaUpdateWrapper<DataHfqxEquipment>().in(DataHfqxEquipment::getDeviceNumber,collect)
            );
        }

        if(CollectionUtil.isNotEmpty(data.getFourList())){
            List<String> collect = data.getFourList().stream().map(HfEquipmentInfo::getDeviceNumber).collect(Collectors.toList());
            dataHfqxEquipmentService.update(new DataHfqxEquipment().setOfflineDays("4")
                    ,new LambdaUpdateWrapper<DataHfqxEquipment>().in(DataHfqxEquipment::getDeviceNumber,collect)
            );
        }

        if(CollectionUtil.isNotEmpty(data.getFiveList())){
            List<String> collect = data.getFiveList().stream().map(HfEquipmentInfo::getDeviceNumber).collect(Collectors.toList());
            dataHfqxEquipmentService.update(new DataHfqxEquipment().setOfflineDays("5")
                    ,new LambdaUpdateWrapper<DataHfqxEquipment>().in(DataHfqxEquipment::getDeviceNumber,collect)
            );
        }

        if(CollectionUtil.isNotEmpty(data.getOverFiveList())){
            List<String> collect = data.getOverFiveList().stream().map(HfEquipmentInfo::getDeviceNumber).collect(Collectors.toList());
            dataHfqxEquipmentService.update(new DataHfqxEquipment().setOfflineDays("超过5")
                    ,new LambdaUpdateWrapper<DataHfqxEquipment>().in(DataHfqxEquipment::getDeviceNumber,collect)
            );
        }
    }
}
