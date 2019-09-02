package com.cnsunrun.common.config;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import com.sunrun.sunrunframwork.utils.EmptyDeal;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * 自定义红包提示消息
 */
@MessageTag(
        value = "app:RedpackReceivedTipMsg",

        flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED
)
public class RedPackReceivedTipMessage extends MessageContent {


    private String tip_content;
    private String redpack_id;
    private int type;

    private String repack_sender_id;
    private String repack_sender_name;
    private String repack_receiver_id;
    private String repack_receiver_name;


    public RedPackReceivedTipMessage(String repack_id, String tip_content) {
        this.redpack_id = repack_id;
        this.tip_content = tip_content;
    }

    public String getTip_content() {
        return EmptyDeal.dealNull(tip_content);
    }

    public void setTip_content(String tip_content) {
        this.tip_content = tip_content;
    }

    public String getRepack_id() {
        return redpack_id;
    }

    public void setRepack_id(String repack_id) {
        this.redpack_id = repack_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public static RedPackReceivedTipMessage obtain(String title, String id) {
        return new RedPackReceivedTipMessage(title, id);
    }


    public RedPackReceivedTipMessage(byte[] data) {
        String jsonStr = new String(data);
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("redpack_id")) {
                this.setRepack_id(jsonObj.optString("redpack_id"));
            }

            if (jsonObj.has("tip_content")) {
                setTip_content(jsonObj.optString("tip_content"));
            }
            if (jsonObj.has("type")) {
                setType(jsonObj.optInt("type"));
            }

            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {


            if (!TextUtils.isEmpty(getRepack_id())) {
                jsonObj.put("repack_id", getRepack_id());
            }
            if (!TextUtils.isEmpty(this.getTip_content())) {
                jsonObj.put("tip_content", this.getTip_content());
            }
            jsonObj.put("type", getType());
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

        } catch (JSONException var3) {
            Log.e("JSONException", var3.getMessage());
        }
        return jsonObj.toString().getBytes();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public RedPackReceivedTipMessage(Parcel in) {
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setRepack_id(ParcelUtils.readFromParcel(in));
        this.setTip_content(ParcelUtils.readFromParcel(in));
        this.setType(ParcelUtils.readIntFromParcel(in));
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getRepack_id());
        ParcelUtils.writeToParcel(dest, this.getTip_content());
        ParcelUtils.writeToParcel(dest, this.getType());
    }

    public static final Creator<RedPackReceivedTipMessage> CREATOR = new Creator<RedPackReceivedTipMessage>() {
        public RedPackReceivedTipMessage createFromParcel(Parcel source) {
            return new RedPackReceivedTipMessage(source);
        }

        public RedPackReceivedTipMessage[] newArray(int size) {
            return new RedPackReceivedTipMessage[size];
        }
    };
}

