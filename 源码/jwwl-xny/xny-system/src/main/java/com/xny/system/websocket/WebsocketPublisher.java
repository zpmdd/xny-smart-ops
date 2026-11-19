package com.xny.system.websocket;

import cn.hutool.json.JSONUtil;
import com.xny.common.utils.R;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

//@Component
public class WebsocketPublisher {
    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 全体发送
     * @param msg
     * @return
     */
    public R<String> sendAllMessage(Object msg){
        webSocketServer.sendAllMessage(JSONUtil.toJsonStr(msg));
        return R.ok();
    }

    /**
     * 单个用户发送
     * @param userId
     * @param msg
     * @return
     */
    public R<String> sendOneMessage(Long userId,Object msg){
        webSocketServer.sendOneMessage(String.valueOf(userId), JSONUtil.toJsonStr(msg));
        return R.ok();
    }

    /**
     * 多个用户发送
     * @param userIdList
     * @param msg
     * @return
     */
    public R<String> sendMoreMessage(List<Long> userIdList, Object msg){
        String[] userIds = (String[])userIdList.toArray();
        webSocketServer.sendMoreMessage(userIds, JSONUtil.toJsonStr(msg));
        return R.ok();
    }
}
