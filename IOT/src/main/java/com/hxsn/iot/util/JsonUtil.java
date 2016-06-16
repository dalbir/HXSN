package com.hxsn.iot.util;

import com.hxsn.iot.model.NotifyInfo;

import org.json.JSONObject;

/**
 * @date Created by jiely on 2016/4/19.
 * * "title":"农情站",
"description":"有新鲜水蜜桃出售，价格优惠",
"notification_builder_id":0,
"notification_basic_style":7,
"open_type":2,	//自定义打开方式
"custom_content":{
"type":1,	//消息类型：1表示农情站资讯信息
"id":"40288ca1548020210154802269e30001"	//资讯ID
 */
public class JsonUtil {


    private static final String[] notifyInfoList = {"id","type","open_type","description","title","notification_builder_id","notification_basic_style"};

    /**
     * desc:通知消息解析
     * auther:jiely
     * create at 2016/5/6 10:52
     */
    public static NotifyInfo getNotifyInfo(String jsonString) {
        NotifyInfo notifyInfo = new NotifyInfo();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            String id = jsonObject.optString(notifyInfoList[0]);
            notifyInfo.setId(id);
            int type = jsonObject.optInt(notifyInfoList[1]);
            notifyInfo.setType(type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifyInfo;
    }


}
