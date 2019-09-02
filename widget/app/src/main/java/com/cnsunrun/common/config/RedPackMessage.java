package com.cnsunrun.common.config;

import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * 自定义红包消息
 */
@MessageTag(
        value = "app:RedpackMsg",
        flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED
)
public class RedPackMessage extends MessageContent {


    private String title;
    private String id;
    private String type;
    private int isRedpackReceived = 0;

    public RedPackMessage(String title, String id,String type) {
        this.title = title;
        this.id = id;
        this.type=type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public RedPackMessage setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRedpackReceived() {
        return isRedpackReceived == 1;
    }

    public int getIsRedpackReceived() {
        return isRedpackReceived;
    }

    public void setRedpackReceived(int redpackReceived) {
        isRedpackReceived = redpackReceived;
    }

    public static RedPackMessage obtain(String title, String id,String type) {
        return new RedPackMessage(title, id,type);
    }


    public RedPackMessage(byte[] data) {
        String jsonStr = new String(data);
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("title")) {
                this.setTitle(jsonObj.optString("title"));
            }

            if (jsonObj.has("id")) {
                setId(jsonObj.optString("id"));
            }
            if (jsonObj.has("type")) {
                setType(jsonObj.optString("type"));
            }
            if (jsonObj.has("isRedpackReceived")) {
                setRedpackReceived(jsonObj.optInt("isRedpackReceived"));
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


            if (!TextUtils.isEmpty(getId())) {
                jsonObj.put("id", getId());
            }
            if (!TextUtils.isEmpty(this.getTitle())) {
                jsonObj.put("title", this.getTitle());
            }
            if (!TextUtils.isEmpty(this.getType())) {
                jsonObj.put("type", this.getType());
            }
            jsonObj.put("isRedpackReceived", isRedpackReceived);
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

    public RedPackMessage(Parcel in) {
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setId(ParcelUtils.readFromParcel(in));
        this.setTitle(ParcelUtils.readFromParcel(in));
        this.setType(ParcelUtils.readFromParcel(in));
        this.setRedpackReceived(ParcelUtils.readIntFromParcel(in));
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getId());
        ParcelUtils.writeToParcel(dest, this.getTitle());
        ParcelUtils.writeToParcel(dest, this.getType());
        ParcelUtils.writeToParcel(dest, this.getIsRedpackReceived());
    }

    public static final Creator<RedPackMessage> CREATOR = new Creator<RedPackMessage>() {
        public RedPackMessage createFromParcel(Parcel source) {
            return new RedPackMessage(source);
        }

        public RedPackMessage[] newArray(int size) {
            return new RedPackMessage[size];
        }
    };
}

