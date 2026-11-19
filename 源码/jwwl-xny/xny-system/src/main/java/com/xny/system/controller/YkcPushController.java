package com.xny.system.controller;

import cn.hutool.json.JSONObject;
import com.xny.system.service.YkcService;
import com.xny.system.ykc.utils.YkcUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/getEquipChargeStatus")
@Api(tags = "云快充数据推送")
public class YkcPushController {
    @Resource
    private YkcService ykcService;

    @ApiOperation("请求token")
    @PostMapping("/query_token")
    public JSONObject queryToken(@RequestBody JSONObject obj){
        return ykcService.queryToken(obj);
    }

    @ApiOperation("设备状态变化推送")
    @PostMapping("/notification_stationStatus")
    public JSONObject stationStatus(@RequestBody JSONObject obj, HttpServletRequest request){
        //YkcUtils.pushTokenVerify(request);
        return ykcService.stationStatus(obj);
    }

    @ApiOperation("推送充电状态")
    @PostMapping("/notification_equip_charge_status")
    public JSONObject chargeStatus(@RequestBody JSONObject obj, HttpServletRequest request){
        //YkcUtils.pushTokenVerify(request);
        return ykcService.chargeStatus(obj);
    }

    @ApiOperation("推送充电订单信息")
    @PostMapping("/notification_charge_order_info")
    public JSONObject chargeOrderInfo(@RequestBody JSONObject obj, HttpServletRequest request){
        //YkcUtils.pushTokenVerify(request);
        return ykcService.chargeOrderInfo(obj);
    }

    @ApiOperation("获取云快充站点信息")
    @PostMapping("/getYkcStationInfo")
    public void getYkcStationInfo(){
        ykcService.getYkcStationInfo();
    }
}
