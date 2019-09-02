package com.cnsunrun.common.util;

import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.quest.Config;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.BaseBeanConvert;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * 自定义数据解析
 * Created by WQ on 2017/5/26.
 */

public class OtherDataConvert extends BaseBeanConvert {

    public static final String STATUS = "status";
    public static final String MSG = "msg";
    public static final String DATA = "info";
    public static final String POST_TIME = "post_time";

    @Override
    public BaseBean convert(NAction action, String json) {
        json = clearError(json);
        BaseBean bean;
        if (action.resultDataType != null) {
            bean = createBean(json, action.resultDataType);
        } else if (action.typeToken != null) {
            bean = createBean(json, action.typeToken);
        } else {
            bean = createBean(json);
        }

        if ((bean.status == 4 ||
                bean.status == 3||bean.status==2) && Config.getLoginInfo().isValid()) {//只在用戶還登錄在的情況調用
            UIUtils.shortM(bean);
            LoginUtil.exitLogin();
            CommonIntent.startLoginActivity();
        }
//        if (bean.status == 2) {//token失效
//            bean.msg = "刷新中..";
//        } else if (bean.status == 0 && "token错误".equals(bean.msg)) {
//            bean.msg = "刷新中..";
//        }
        bean.tag = action.getTag();
        return bean;
    }

    public static BaseBean createBean(String json, Class<?> clazz) {
        JSONObject jobj = JsonDeal.createJsonObj(json);
        BaseBean bean = new BaseBean();
        bean.status = jobj.optInt(STATUS);
        bean.msg = jobj.optString(MSG);
        Object obj = jobj.opt(DATA);
        bean.post_time = jobj.optString(POST_TIME);
        if (bean.status == 1) {
            bean.data = JsonDeal.json2Object(String.valueOf(obj), clazz);
        }

        return bean;
    }

    public static <T> BaseBean createBean(String json, TypeToken<T> typeToken) {
        JSONObject jobj = JsonDeal.createJsonObj(json);
        BaseBean bean = new BaseBean();
        bean.status = jobj.optInt(STATUS);
        bean.msg = jobj.optString(MSG);
        Object obj = jobj.opt(DATA);
        bean.post_time = jobj.optString(POST_TIME);
        if (bean.status == 1) {
            bean.data = JsonDeal.json2Object(String.valueOf(obj), typeToken);
        }

        return bean;
    }

    public static <T> BaseBean createBean(String json) {
        JSONObject jobj = JsonDeal.createJsonObj(json);
        BaseBean bean = new BaseBean();
        bean.status = jobj.optInt(STATUS);
        bean.msg = jobj.optString(MSG);
        Object obj = jobj.has(DATA) ? jobj.opt(DATA) : json;
        bean.post_time = jobj.optString("post_time");
        bean.data = String.valueOf(obj);
        return bean;
    }

    public String clearError(String json) {
        return clearError(JsonDeal.createJsonObj(json));
    }

    public String clearError(JSONObject jsonObject) {
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                Object val = jsonObject.get(key);
                if (val instanceof JSONObject) {
                    clearError((JSONObject) val);
                } else if (val instanceof JSONArray) {
                    JSONArray jArray = (JSONArray) val;
                    clearError(jArray);
                } else {
                    if ("".equals(val)) {
                        iterator.remove();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public void clearError(JSONArray jArray) {
        for (int i = 0, len = jArray.length(); i < len; i++) {

            try {
                Object jobj = jArray.get(i);
                if (jobj instanceof JSONObject) {
                    clearError((JSONObject) jobj);
                } else if (jobj instanceof JSONArray) {
                    clearError((JSONArray) jobj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
