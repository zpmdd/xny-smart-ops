package com.xny.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xny.common.utils.R;
import com.xny.common.utils.SnowIdUtil;
import com.xny.system.entity.*;
import com.xny.system.mapper.SysParamSimulateMapper;
import com.xny.system.model.dto.DataRecieveDTO;
import com.xny.system.model.dto.ListParamSimulateDTO;
import com.xny.system.model.vo.ListParamSimulateVO;
import com.xny.system.service.NanRuiService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class NanRuiServiceImpl implements NanRuiService {
    private static final List<Long> syParamIdList = Arrays.asList(110003L, 110041L, 110004L);
    private static final List<Long> hourParamIdList  = Arrays.asList(110021L, 110003L);
    private static final List<Long> monthParamIdList  = Arrays.asList(110021L, 110041L);

    @Resource
    private SysParamSimulateMapper sysParamSimulateMapper;

    @Override
    public R<String> dataRecieve(DataRecieveDTO dto) {
        List<String> codeDataList = new ArrayList<>(dto.getData().keySet());
        if(CollectionUtil.isEmpty(codeDataList)){
            return R.failed("数据不能为空");
        }

        List<ListParamSimulateVO> listParamSimulate = sysParamSimulateMapper.listParamSimulate(
                new ListParamSimulateDTO().setSubmatrixSection(dto.getArea()).setDataCodeList(codeDataList)
        );
        if(CollectionUtil.isEmpty(listParamSimulate)){
            return R.failed("未查询到对应参数信息");
        }

        Map<String, ListParamSimulateVO> simulateMap = listParamSimulate.stream().collect(Collectors.toMap(ListParamSimulateVO::getDataCode, t -> t));
        Double syFactor = sysParamSimulateMapper.selectById(102L).getParamBaseValue();
        Double swFactor = sysParamSimulateMapper.selectById(101L).getParamBaseValue();

        List<DataDeviceRealTime> realTimeDataList = new ArrayList<>();
        List<DataDeviceHour> hourDataList = new ArrayList<>();
        List<DataDeviceMonth> monthDataList = new ArrayList<>();

        for (String key : codeDataList) {
            if(!simulateMap.containsKey(key)){
                continue;
            }

            String value = dto.getData().get(key);
            if(StrUtil.isBlank(value)){
                continue;
            }

            ListParamSimulateVO vo = simulateMap.get(key);
            Date date = new Date();
            String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");

            //记录实时值
            DataDeviceRealTime dataDeviceRealTime = new DataDeviceRealTime();
            dataDeviceRealTime.setId(SnowIdUtil.nextId());
            dataDeviceRealTime.setDeviceId(vo.getDeviceId());
            dataDeviceRealTime.setParamId(vo.getParamId());
            dataDeviceRealTime.setParamValue(Double.valueOf(value));
            dataDeviceRealTime.setParamTime(date);
            realTimeDataList.add(dataDeviceRealTime);

            //计算收益
            if(syParamIdList.contains(vo.getParamId())){
                if(vo.getParamId().equals(110003L)){
                    DataDeviceRealTime syRealTime = new DataDeviceRealTime();
                    syRealTime.setId(SnowIdUtil.nextId());
                    syRealTime.setDeviceId(vo.getDeviceId());
                    syRealTime.setParamTime(date);
                    syRealTime.setParamId(100002L);
                    syRealTime.setParamValue(Double.parseDouble(value)*syFactor);
                    realTimeDataList.add(syRealTime);

                }else if(vo.getParamId().equals(110041L)){
                    DataDeviceRealTime syRealTime = new DataDeviceRealTime();
                    syRealTime.setId(SnowIdUtil.nextId());
                    syRealTime.setDeviceId(vo.getDeviceId());
                    syRealTime.setParamTime(date);
                    syRealTime.setParamId(100003L);
                    syRealTime.setParamValue(Double.parseDouble(value)*syFactor/10000);
                    realTimeDataList.add(syRealTime);

                }else if(vo.getParamId().equals(110004L)){
                    DataDeviceRealTime syRealTime = new DataDeviceRealTime();
                    syRealTime.setId(SnowIdUtil.nextId());
                    syRealTime.setDeviceId(vo.getDeviceId());
                    syRealTime.setParamTime(date);
                    syRealTime.setParamId(100005L);
                    syRealTime.setParamValue(Double.parseDouble(value)*syFactor/10000);
                    realTimeDataList.add(syRealTime);

                    DataDeviceRealTime swRealTime = new DataDeviceRealTime();
                    swRealTime.setId(SnowIdUtil.nextId());
                    swRealTime.setDeviceId(vo.getDeviceId());
                    swRealTime.setParamTime(date);
                    swRealTime.setParamId(100008L);
                    swRealTime.setParamValue(Double.parseDouble(value)*swFactor);
                    realTimeDataList.add(swRealTime);
                }
            }


            //记录小时值
            if(hourParamIdList.contains(vo.getParamId())){
                DataDeviceHour dataDeviceHour = new DataDeviceHour();
                dataDeviceHour.setId(SnowIdUtil.nextId());
                dataDeviceHour.setDeviceId(vo.getDeviceId());
                dataDeviceHour.setParamId(vo.getParamId());
                dataDeviceHour.setParamValue(Double.valueOf(value));
                dataDeviceHour.setParamTime(DateUtil.parse(format,"yyyy-MM-dd HH"));
                hourDataList.add(dataDeviceHour);
            }

            //记录月值
            if(monthParamIdList.contains(vo.getParamId())){
                DataDeviceMonth dataDeviceMonth = new DataDeviceMonth();
                dataDeviceMonth.setId(SnowIdUtil.nextId());
                dataDeviceMonth.setDeviceId(vo.getDeviceId());
                dataDeviceMonth.setParamId(vo.getParamId());
                dataDeviceMonth.setParamValue(Double.valueOf(value));
                dataDeviceMonth.setParamTime(DateUtil.parse(format,"yyyy-MM"));
                monthDataList.add(dataDeviceMonth);

                if(vo.getParamId().equals(110041L)){
                    DataDeviceMonth totalSyMonth = new DataDeviceMonth();
                    totalSyMonth.setId(SnowIdUtil.nextId());
                    totalSyMonth.setDeviceId(vo.getDeviceId());
                    totalSyMonth.setParamId(100003L);
                    totalSyMonth.setParamValue(Double.parseDouble(value)*syFactor/10000);
                    totalSyMonth.setParamTime(DateUtil.parse(format,"yyyy-MM"));
                    monthDataList.add(totalSyMonth);
                }
            }
        }

        if(CollectionUtil.isNotEmpty(realTimeDataList)){
            sysParamSimulateMapper.realTimeBatchInsertOrUpdate(realTimeDataList);
        }

        if(CollectionUtil.isNotEmpty(hourDataList)){
            sysParamSimulateMapper.hourBatchInsertOrUpdate(hourDataList);
        }

        if(CollectionUtil.isNotEmpty(monthDataList)){
            sysParamSimulateMapper.monthBatchInsertOrUpdate(monthDataList);
        }

        return R.ok();
    }

}
