package com.xny.system.hfqx.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.RedisUtils;
import com.xny.system.hfqx.config.HfqxConfig;
import com.xny.system.hfqx.model.vo.DeviceInfoListVO;
import com.xny.system.hfqx.model.vo.DeviceLKLocationVO;
import com.xny.system.hfqx.model.vo.LoginVO;
import com.xny.system.hfqx.model.vo.LongTimeOffLineVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@Slf4j
public class HfqxUtils {
    private static HfqxConfig hfqxConfig;
    private static RedisUtils redisUtils;

    @Resource
    public void setHfqxConfig(HfqxConfig hfqxConfig) {
        HfqxUtils.hfqxConfig = hfqxConfig;
    }

    @Resource
    public void setRedisUtils(RedisUtils redisUtils) {
        HfqxUtils.redisUtils = redisUtils;
    }

    /**
     * 登录
     * @return
     */
    private static String login(){
        JSONObject param = new JSONObject();
        param.putOnce("AppKey",hfqxConfig.getAppKey());
        param.putOnce("LoginName",hfqxConfig.getLoginName());
        param.putOnce("LoginPassword",hfqxConfig.getLoginPassword());
        String send = send(param, "data/Login");

        LoginVO loginVO = JSONUtil.toBean(send, LoginVO.class);
        String userCode = loginVO.getData().getUserCode();
        redisUtils.set("UserCode",userCode,86400L);

        return userCode;
    }

    /**
     * 获取用户编码
     * @return
     */
    private static String getUserCode(){
        Object ykcToken = redisUtils.get("UserCode");
        if(BeanUtil.isEmpty(ykcToken)){
            return login();
        }
        return String.valueOf(ykcToken);
    }


    /**
     * 设备列表
     * @return
     */
    public static DeviceInfoListVO getDeviceInfoList(){
        JSONObject param = new JSONObject();
        param.putOnce("AppKey",hfqxConfig.getAppKey());
        param.putOnce("UserCode",getUserCode());
        String send = send(param, "Data/GetDeviceInfoList");
        //String send  = "{\"Code\":0,\"Message\":\"获取成功！\",\"Data\":[{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":0,\"IsLocation\":1,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":22.635848,\"GLng\":113.926861,\"StopTime\":2741,\"OfflineTime\":14283054,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"C1\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"20220421002\",\"GroupCode\":\"1000008928\",\"SIM\":\"20220421002\",\"ServicePlan\":0,\"DeviceType\":18,\"SubType\":-1,\"ExpDate\":\"2025-03-29 23:57:08\",\"IsValid\":2,\"IsFocus\":1,\"ServiceLimit\":0,\"UpdateTime\":\"2023-10-10 22:16:17\",\"IsOnline\":0,\"Plate\":\"二号清扫机器人\",\"DeviceName\":\"二号清扫机器人\",\"LocationTime\":\"C1：B点等待剩余9分钟\"},{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":0,\"IsLocation\":1,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":22.636116,\"GLng\":113.926972,\"StopTime\":29884,\"OfflineTime\":60084028,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"C1\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"20220322003\",\"GroupCode\":\"1000008928\",\"SIM\":\"13782865854\",\"ServicePlan\":0,\"DeviceType\":18,\"SubType\":-1,\"ExpDate\":\"2025-03-29 23:57:08\",\"IsValid\":2,\"IsFocus\":1,\"ServiceLimit\":0,\"UpdateTime\":\"2023-10-10 22:16:35\",\"IsOnline\":1,\"Plate\":\"三号清扫机器人\",\"DeviceName\":\"三号清扫机器人\",\"LocationTime\":\"C1：离开A点0分钟\"},{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":0,\"IsLocation\":1,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":22.524101,\"GLng\":113.404751,\"StopTime\":200000,\"OfflineTime\":481729,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"C3\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"20220421004\",\"GroupCode\":\"1000008928\",\"SIM\":\"20220421004\",\"ServicePlan\":0,\"DeviceType\":18,\"SubType\":-1,\"ExpDate\":\"2025-03-29 23:57:08\",\"IsValid\":1,\"IsFocus\":1,\"ServiceLimit\":0,\"UpdateTime\":\"2023-11-30 18:19:04\",\"IsOnline\":0,\"Plate\":\"四号清扫机器人\",\"DeviceName\":\"四号清扫机器人\",\"LocationTime\":\"检修：离开A点0分钟\"},{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":0,\"IsLocation\":1,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":34.496329,\"GLng\":109.522569,\"StopTime\":623798,\"OfflineTime\":16194908,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"C1\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"20220421001\",\"GroupCode\":\"1000008928\",\"SIM\":\"\",\"ServicePlan\":0,\"DeviceType\":18,\"SubType\":-1,\"ExpDate\":\"2025-03-29 23:57:08\",\"IsValid\":2,\"IsFocus\":0,\"ServiceLimit\":0,\"UpdateTime\":\"2023-10-10 22:16:08\",\"IsOnline\":0,\"Plate\":\"一号清扫机器人\",\"DeviceName\":\"一号清扫机器人\",\"LocationTime\":\"C1：离开B点0分钟\"}]}";
        return JSONUtil.toBean(send, DeviceInfoListVO.class);
    }


    /**
     * 全部设备实时位置
     * @return
     */
    public static DeviceLKLocationVO getUserDeviceLKLocation(){
        JSONObject param = new JSONObject();
        param.putOnce("AppKey",hfqxConfig.getAppKey());
        param.putOnce("UserCode",getUserCode());
        String send = send(param, "Data/GetUserDeviceLKLocation");
        //String send = "{\"Code\":0,\"Message\":\"获取成功！\",\"Data\":[{\"RobotWorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"WaitingTime\":0,\"IsStart\":0,\"IsStartRun\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"IsEndRun\":0,\"T0Value\":0,\"T1Value\":0,\"T2Value\":0,\"TValue\":0,\"CurrentControl\":0,\"LocationID\":0,\"DeviceNumber\":\"20220421004\",\"DeviceType\":30,\"UTCTime\":\"2024-03-24 02:13:01\",\"SIM\":\"20220421004\",\"IsValid\":1,\"GLat\":22.524101,\"GLng\":113.404751,\"OLat\":22.526788,\"OLng\":113.399267,\"BLat\":22.530427,\"BLng\":113.411167,\"Speed\":0,\"Course\":0,\"IsStop\":0,\"Distance\":0,\"StopTime\":200000,\"Address\":\"\",\"LocalTime\":\"2024-03-24 10:13:01\",\"PowerStatus\":0,\"IsOnline\":0,\"Plate\":\"四号清扫机器人\",\"GroupName\":\"Default\",\"BAT\":0,\"ReportInterval\":\"0\",\"RecordCnt\":0,\"LocationModel\":0,\"AlarmContent\":\"\",\"TimePoint\":\"\",\"DeviceStatus\":7,\"IsDisplaySetup\":0},{\"RobotWorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"WaitingTime\":0,\"IsStart\":0,\"IsStartRun\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"IsEndRun\":0,\"T0Value\":0,\"T1Value\":0,\"T2Value\":0,\"TValue\":0,\"CurrentControl\":0,\"LocationID\":162486,\"DeviceNumber\":\"20220322003\",\"DeviceType\":30,\"UTCTime\":\"2022-05-04 06:01:22\",\"SIM\":\"13782865854\",\"IsValid\":2,\"GLat\":22.636116,\"GLng\":113.926972,\"OLat\":22.639143,\"OLng\":113.922104,\"BLat\":22.641843,\"BLng\":113.933509,\"Speed\":0,\"Course\":76,\"IsStop\":1,\"Distance\":0,\"StopTime\":29884,\"Address\":\"\",\"LocalTime\":\"2022-05-04 14:01:22\",\"PowerStatus\":0,\"IsOnline\":1,\"Plate\":\"三号清扫机器人\",\"GroupName\":\"Default\",\"BAT\":0,\"ReportInterval\":\"0\",\"RecordCnt\":0,\"LocationModel\":0,\"AlarmContent\":\"\",\"TimePoint\":\"\",\"DeviceStatus\":7,\"IsDisplaySetup\":0},{\"RobotWorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"WaitingTime\":0,\"IsStart\":0,\"IsStartRun\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"IsEndRun\":0,\"T0Value\":0,\"T1Value\":0,\"T2Value\":0,\"TValue\":0,\"CurrentControl\":0,\"LocationID\":162489,\"DeviceNumber\":\"20220421001\",\"DeviceType\":30,\"UTCTime\":\"2023-09-24 05:26:42\",\"SIM\":\"\",\"IsValid\":2,\"GLat\":34.496329,\"GLng\":109.522569,\"OLat\":34.497696,\"OLng\":109.517467,\"BLat\":34.502222,\"BLng\":109.529129,\"Speed\":0,\"Course\":0,\"IsStop\":1,\"Distance\":0,\"StopTime\":623798,\"Address\":\"\",\"LocalTime\":\"2023-09-24 13:26:42\",\"PowerStatus\":0,\"IsOnline\":1,\"Plate\":\"一号清扫机器人\",\"GroupName\":\"Default\",\"BAT\":0,\"ReportInterval\":\"0\",\"RecordCnt\":0,\"LocationModel\":0,\"AlarmContent\":\"\",\"TimePoint\":\"\",\"DeviceStatus\":7,\"IsDisplaySetup\":0},{\"RobotWorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"WaitingTime\":0,\"IsStart\":0,\"IsStartRun\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"IsEndRun\":0,\"T0Value\":0,\"T1Value\":0,\"T2Value\":0,\"TValue\":0,\"CurrentControl\":0,\"LocationID\":162490,\"DeviceNumber\":\"20220421002\",\"DeviceType\":30,\"UTCTime\":\"2023-10-16 08:30:56\",\"SIM\":\"20220421002\",\"IsValid\":2,\"GLat\":22.635848,\"GLng\":113.926861,\"OLat\":22.638876,\"OLng\":113.921993,\"BLat\":22.641577,\"BLng\":113.933398,\"Speed\":0,\"Course\":306,\"IsStop\":1,\"Distance\":0,\"StopTime\":2741,\"Address\":\"\",\"LocalTime\":\"2023-10-16 16:30:56\",\"PowerStatus\":0,\"IsOnline\":1,\"Plate\":\"二号清扫机器人\",\"GroupName\":\"Default\",\"BAT\":0,\"ReportInterval\":\"0\",\"RecordCnt\":0,\"LocationModel\":0,\"AlarmContent\":\"\",\"TimePoint\":\"\",\"DeviceStatus\":7,\"IsDisplaySetup\":0}]}";
        return JSONUtil.toBean(send, DeviceLKLocationVO.class);
    }


    /**
     * 长时间未上线
     * @return
     */
    public static LongTimeOffLineVO getLongTimeOffLine(){
        JSONObject param = new JSONObject();
        param.putOnce("AppKey",hfqxConfig.getAppKey());
        param.putOnce("UserCode",getUserCode());
        String send = send(param, "Data/GetLongTimeOffLine");
        //String send = "{\"Code\":0,\"Message\":\"获取成功！\",\"Data\":{\"ThreeList\":[],\"FourList\":[],\"FiveList\":[],\"OverFiveList\":[{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":1,\"IsLocation\":0,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":0,\"GLng\":0,\"StopTime\":0,\"OfflineTime\":0,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"20220421004\",\"GroupCode\":\"0\",\"SIM\":\"\",\"ServicePlan\":0,\"DeviceType\":0,\"SubType\":-1,\"ExpDate\":\"2024-03-30 00:06:15\",\"IsValid\":0,\"IsFocus\":0,\"ServiceLimit\":0,\"UpdateTime\":\"2024-03-30 00:06:15\",\"IsOnline\":0,\"Plate\":\"\",\"DeviceName\":\"\",\"LocationTime\":\"\"},{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":1,\"IsLocation\":0,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":0,\"GLng\":0,\"StopTime\":0,\"OfflineTime\":0,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"\",\"GroupCode\":\"0\",\"SIM\":\"\",\"ServicePlan\":0,\"DeviceType\":0,\"SubType\":-1,\"ExpDate\":\"2024-03-30 00:06:15\",\"IsValid\":0,\"IsFocus\":0,\"ServiceLimit\":0,\"UpdateTime\":\"2024-03-30 00:06:15\",\"IsOnline\":0,\"Plate\":\"\",\"DeviceName\":\"\",\"LocationTime\":\"\"},{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":1,\"IsLocation\":0,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":0,\"GLng\":0,\"StopTime\":0,\"OfflineTime\":0,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"\",\"GroupCode\":\"0\",\"SIM\":\"\",\"ServicePlan\":0,\"DeviceType\":0,\"SubType\":-1,\"ExpDate\":\"2024-03-30 00:06:15\",\"IsValid\":0,\"IsFocus\":0,\"ServiceLimit\":0,\"UpdateTime\":\"2024-03-30 00:06:15\",\"IsOnline\":0,\"Plate\":\"\",\"DeviceName\":\"\",\"LocationTime\":\"\"},{\"Children\":[],\"Description\":\"\",\"PowerStatus\":0,\"Speed\":0,\"IsExpired\":1,\"IsLocation\":0,\"Protocol\":0,\"UTCTimeStr\":\"\",\"SpeedStr\":\"\",\"GLat\":0,\"GLng\":0,\"StopTime\":0,\"OfflineTime\":0,\"DeviceTypeName\":\"\",\"DeviceModelName\":\"\",\"AlarmContent\":\"\",\"VehicleInfoName\":\"\",\"Address\":\"\",\"WorkStatus\":\"\",\"ConfigTime\":\"\",\"CurrentTime\":\"\",\"NormalStatus\":0,\"WaitingTime\":0,\"IsStart\":0,\"StartTime\":0,\"IsEnd\":0,\"EndTime\":0,\"UserName\":\"\",\"DeviceNumber\":\"\",\"GroupCode\":\"0\",\"SIM\":\"\",\"ServicePlan\":0,\"DeviceType\":0,\"SubType\":-1,\"ExpDate\":\"2024-03-30 00:06:15\",\"IsValid\":0,\"IsFocus\":0,\"ServiceLimit\":0,\"UpdateTime\":\"2024-03-30 00:06:15\",\"IsOnline\":0,\"Plate\":\"\",\"DeviceName\":\"\",\"LocationTime\":\"\"}],\"OverFiveCount\":4}}";
        return JSONUtil.toBean(send, LongTimeOffLineVO.class);
    }

    /**
     * 发送请求
     * @param param
     * @param url
     * @return
     */
    public static String send(JSONObject param,String url) {
        String sParam = JSONUtil.toJsonStr(param);
        log.info(url+"======请求参数:"+sParam);

        String body = HttpRequest.post(hfqxConfig.getHfqxUrl()+url)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param))
                .execute()
                .body();

        log.info(url+"======返回参数:"+body);

        JSONObject oBody = JSONUtil.parseObj(body);
        String code = oBody.getStr("Code");
        if(StrUtil.isBlank(code) || !code.equals("0")){
            throw new RuntimeException(url + "======请求失败:"+code);
        }
        return body;
    }
}
