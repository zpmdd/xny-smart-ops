package com.xny.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xny.common.utils.R;
import com.xny.system.entity.DataHfqxEquipment;
import com.xny.system.mapper.DataHfqxEquipmentMapper;
import com.xny.system.model.dto.DetailEquipmentDTO;
import com.xny.system.model.dto.HfqxGlobalViewDTO;
import com.xny.system.model.dto.ListEquipmentDTO;
import com.xny.system.model.vo.*;
import com.xny.system.service.IDataHfqxEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 红枫清洗设备数据 服务实现类
 * </p>
 *
 * @author jwwl
 * @since 2024-05-24
 */
@Service
public class DataHfqxEquipmentServiceImpl extends ServiceImpl<DataHfqxEquipmentMapper, DataHfqxEquipment> implements IDataHfqxEquipmentService {
    @Resource
    private DataHfqxEquipmentMapper dataHfqxEquipmentMapper;

    @Override
    public HfqxGlobalViewVO hfqxGlobalView(HfqxGlobalViewDTO dto) {
        HfqxGlobalViewVO hfqxGlobalViewVO = new HfqxGlobalViewVO();

        CountEquipmentVO countEquipment= dataHfqxEquipmentMapper.countEquipment();
        List<ListEquipmentVO> ltOfflineEquipmentList = dataHfqxEquipmentMapper.listEquipment(new ListEquipmentDTO().setOfflineStatus(0));
        List<ListEquipmentVO> onlineEquipmentList = dataHfqxEquipmentMapper.listEquipment(new ListEquipmentDTO().setOfflineStatus(1));

        int count = 3-ltOfflineEquipmentList.size();

//        hfqxGlobalViewVO.setDataView(new ItemVO().setChilds(Arrays.asList(
//                new ItemVO().setLabel("设备总数量").setUnit("台").setValue(String.valueOf(countEquipment.getTotalCount()+count)),
//                new ItemVO().setLabel("当前在线设备").setUnit("台").setValue(String.valueOf(countEquipment.getOnlineCount())),
//                new ItemVO().setLabel("当前离线设备").setUnit("台").setValue(String.valueOf(countEquipment.getOfflineCount()+count))
//        )).setLabel("数据一览"));

        hfqxGlobalViewVO.setDataView(new ItemVO().setChilds(Arrays.asList(
                new ItemVO().setLabel("设备总数量").setUnit("台").setValue("3"),
                new ItemVO().setLabel("当前在线设备").setUnit("台").setValue("3"),
                new ItemVO().setLabel("当前离线设备").setUnit("台").setValue("0")
        )).setLabel("数据一览"));


//        if(ltOfflineEquipmentList.size() < 3){
//            for (int i = 1; i<= count; i++) {
//                ListEquipmentVO listEquipmentVO = new ListEquipmentVO();
//                listEquipmentVO.setDeviceNumber("1230501168"+i);
//                listEquipmentVO.setDeviceName((i+1)+"号清洗机器人");
//                listEquipmentVO.setOfflineDays(String.valueOf(i));
//                ltOfflineEquipmentList.add(listEquipmentVO);
//            }
//        }
//        hfqxGlobalViewVO.setLtOffLineEquipmentData(new ItemVO().setChilds(getItemVoList(ltOfflineEquipmentList))
//                .setLabel("离线设备信息"));

        hfqxGlobalViewVO.setOnlineEquipmentData(new ItemVO().setChilds(getItemVoList(onlineEquipmentList))
                .setLabel("在线设备信息"));

        return hfqxGlobalViewVO;
    }

    @Override
    public DetailEquipmentVO detailEquipment(DetailEquipmentDTO dto) {
        DetailEquipmentVO detailEquipmentVO = new DetailEquipmentVO();
        DataHfqxEquipment equipment = this.getById(dto.getDeviceNumber());

        if(equipment.getWorkMode().equals("C1")){
            equipment.setWorkMode("本地模式1");
        }else if(equipment.getWorkMode().equals("C2")){
            equipment.setWorkMode("本地模式2");
        }

        if(equipment.getDeviceName().equals("西安研究所")){
            equipment.setDeviceName("1号清洗机器人");
        }

        if(StrUtil.isBlank(equipment.getRealTimeAddress())){
            equipment.setRealTimeAddress("陕西省西安市雁塔区雁翔路99号");
        }

        DateTime dateTime = DateUtil.offsetHour(DateUtil.beginOfDay(new Date()), 8);

        List<ItemVO> itemVOS = new ArrayList<>(Arrays.asList(
                new ItemVO().setLabel("设备名称").setValue(equipment.getDeviceName()),
                new ItemVO().setLabel("设备编码").setValue(equipment.getDeviceNumber()),
                new ItemVO().setLabel("工作模式").setValue(equipment.getWorkMode()),
                new ItemVO().setLabel("工作状态").setValue("工作中"),
                new ItemVO().setLabel("起点").setValue(equipment.getStartAddress()),
                new ItemVO().setLabel("是否在起点").setValue(equipment.getIsStart()==0 ? "否":"是"),
                new ItemVO().setLabel("实时位置").setValue(equipment.getRealTimeAddress()),
                new ItemVO().setLabel("终点").setValue(equipment.getEndAddress()),
                new ItemVO().setLabel("是否在终点").setValue(equipment.getIsEnd()==0 ? "否":"是"),
                new ItemVO().setLabel("启动时间").setValue(DateUtil.format(dateTime,"yyyy-MM-dd HH:mm:ss"))
        ));

        detailEquipmentVO.setEquipmentDetail(itemVOS);

        return detailEquipmentVO;
    }

    /**
     * 获取设备列表
     * @param list
     * @return
     */
    private List<ItemVO> getItemVoList(List<ListEquipmentVO> list) {
        List<ItemVO> itemVoList = new ArrayList<>();
        for (ListEquipmentVO vo : list) {
            ItemVO itemVO = new ItemVO().setChilds(Arrays.asList(
                    new ItemVO().setLabel("设备名称").setValue(vo.getDeviceName()),
                    new ItemVO().setLabel("设备编码").setValue(vo.getDeviceNumber()),
                    new ItemVO().setLabel("离线天数").setUnit("天").setValue(vo.getOfflineDays())
            ));
            itemVoList.add(itemVO);
        }
        return itemVoList;
    }

}
