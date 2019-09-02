package com.cnsunrun.common.model;

import com.cnsunrun.common.quest.Config;
import com.sunrun.sunrunframwork.utils.DateUtil;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.Utils;

import static com.cnsunrun.common.quest.TokenRequestPreProccess.addToken;

/**
 * Created by cnsunrun on 2017/5/26.
 * <p>
 * 登录模型类
 */

public class LoginInfo {

    /**
     * id : 2
     * mobile : 18627784626
     * username :
     * password : d88244ec1f2807a8f7d8043fad94e2ec
     * is_default_password : 0
     * token : {"token_type":"Bearer","expires_time":"2017-11-20 17:46:50","access_token":"eyJ0eXBlIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5faWQiOiI0MDZjYWI3N2QxZThlZTg5ZDliNjI2YWFmNWFiNDVmNCIsImFkZF90aW1lIjoiMjAxNy0xMS0yMCAxNTo0Njo1MCIsImV4cGlyZXNfdGltZSI6IjIwMTctMTEtMjAgMTc6NDY6NTAiLCJtZW1iZXJfaWQiOiIyIn0=.O/bag/9yK77tAuddOrjU6qoZ+1Z70TNTfn3/HfXjurwiqcX1XBaZPprhaY+aDx6sFpk/jk/cH9Dd0t/mXL2cBRysOUuMIufJ5GYxC3kxT+GbeP8cz9SfKicgbtDCMAsvLyr11s0VSvokl71D7HaCWDDawQYeAQmFAF1htmoVQF3H8JmgqkgZSkg4JbNDvlxbT0axekEBmuvQeAAZaE8R9BcAPOeqiAkirDuLpuOewioa6/vFLFpMRdlOioecnsI0PvaHP96nktocR8jrxPN/JsQLd4iUyHZk4m/L8stHrvqXiE/vvCAAcTqlu/fB1HUslKGuozjlhPE3lnxsPiqVOQ==","refresh_token":"eyJ0eXBlIjoicmVmcmVzaF90b2tlbiIsInRva2VuX2lkIjoiODY5MmM4ZjA1NDczMTA4YTI2ODIyMjRlYTc1YWMzY2QiLCJhZGRfdGltZSI6IjIwMTctMTEtMjAgMTU6NDY6NTAiLCJleHBpcmVzX3RpbWUiOiIyMDE3LTExLTIwIDE4OjQ2OjUwIn0=.giOI7tyhJ8aVPWOtA4R+XbjTZ9zTAcAsT+n8Ef6lFVPcqYEjm5d5FKIi8Zfff4ETkeZPsPCfzx7Iajb45kwrQRK2HdvPBI9XFjyTKQq24eeDxvHO57VJkEOuURR/aSyXpssxlWIxjtdk3k+3ttjhLKhwJSoX0eyHD4zxhlJ9+oX59NG8TKqKfHQD4ZxOxZ2FZ/NQ/Kmeych54Yh+QeJYJAeb/di75iOdpmDrEvkGfCyHTV6RsomZOR4kHaxPCiBW+64UG23SUfAo9u7geu7esQfewUB80EI6mDJpIP1dJmVlz2KLTR7pnz5q/V9HXcGeYX6CI6KRo9iXm0qes81YXA=="}
     */

    public String id;
    public String mobile;
    public String username;
    public String password;
    public String is_default_password;
    /**
     * token_type : Bearer
     * expires_time : 2017-11-20 17:46:50
     * access_token : eyJ0eXBlIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5faWQiOiI0MDZjYWI3N2QxZThlZTg5ZDliNjI2YWFmNWFiNDVmNCIsImFkZF90aW1lIjoiMjAxNy0xMS0yMCAxNTo0Njo1MCIsImV4cGlyZXNfdGltZSI6IjIwMTctMTEtMjAgMTc6NDY6NTAiLCJtZW1iZXJfaWQiOiIyIn0=.O/bag/9yK77tAuddOrjU6qoZ+1Z70TNTfn3/HfXjurwiqcX1XBaZPprhaY+aDx6sFpk/jk/cH9Dd0t/mXL2cBRysOUuMIufJ5GYxC3kxT+GbeP8cz9SfKicgbtDCMAsvLyr11s0VSvokl71D7HaCWDDawQYeAQmFAF1htmoVQF3H8JmgqkgZSkg4JbNDvlxbT0axekEBmuvQeAAZaE8R9BcAPOeqiAkirDuLpuOewioa6/vFLFpMRdlOioecnsI0PvaHP96nktocR8jrxPN/JsQLd4iUyHZk4m/L8stHrvqXiE/vvCAAcTqlu/fB1HUslKGuozjlhPE3lnxsPiqVOQ==
     * refresh_token : eyJ0eXBlIjoicmVmcmVzaF90b2tlbiIsInRva2VuX2lkIjoiODY5MmM4ZjA1NDczMTA4YTI2ODIyMjRlYTc1YWMzY2QiLCJhZGRfdGltZSI6IjIwMTctMTEtMjAgMTU6NDY6NTAiLCJleHBpcmVzX3RpbWUiOiIyMDE3LTExLTIwIDE4OjQ2OjUwIn0=.giOI7tyhJ8aVPWOtA4R+XbjTZ9zTAcAsT+n8Ef6lFVPcqYEjm5d5FKIi8Zfff4ETkeZPsPCfzx7Iajb45kwrQRK2HdvPBI9XFjyTKQq24eeDxvHO57VJkEOuURR/aSyXpssxlWIxjtdk3k+3ttjhLKhwJSoX0eyHD4zxhlJ9+oX59NG8TKqKfHQD4ZxOxZ2FZ/NQ/Kmeych54Yh+QeJYJAeb/di75iOdpmDrEvkGfCyHTV6RsomZOR4kHaxPCiBW+64UG23SUfAo9u7geu7esQfewUB80EI6mDJpIP1dJmVlz2KLTR7pnz5q/V9HXcGeYX6CI6KRo9iXm0qes81YXA==
     */

    public TokenBean token;
    public String community_id;
    public String district_id;
    public String pwd;
    public String imName;
    public String imPwd;

    public LoginInfo setPwd(String pwd) {
        this.pwd = pwd;
        return saveSelf();
    }

    public String getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public LoginInfo setCommunity_id(String community_id) {
        this.community_id = community_id;
        return saveSelf();
    }

    public String getDistrict_id() {
        return district_id;
    }

    public LoginInfo setDistrict_id(String district_id) {
        this.district_id = district_id;
        return saveSelf();
    }

    public LoginInfo setIs_default_password(String is_default_password) {
        this.is_default_password = is_default_password;
        return saveSelf();
    }

    public boolean is_default_password() {
        return "1".equals(is_default_password);
    }

    public LoginInfo setToken(TokenBean token) {
        this.token = token;
        LoginInfo loginInfo = saveSelf();
        addToken();
        return loginInfo;
    }

    /**
     * token时间是否有效
     * @return
     */
    public boolean isExpiresTime(){
        if(!isValid())return true;//有效.未登陆不检查
       return token!=null&& ((DateUtil.getDateByFormat(token.expires_time,DateUtil.dateFormatYMDHMS).getTime()-System.currentTimeMillis())>60*60*1000);//
    }

    public String getUsername() {
        return username;
    }

    public LoginInfo setUsername(String username) {
        this.username = username;    return saveSelf();
    }

    public String getCommunity_id() {
        return community_id;
    }

    private LoginInfo saveSelf() {
        Config.putLoginInfo(this);
        return this;
    }

    public static class TokenBean {
        public String token_type;
        public String expires_time;
        public String access_token;
        public String refresh_token;
    }


    public String getImName() {
        return imName;
    }

    public String getImPwd() {
        return imPwd;
    }

    public LoginInfo setImName(String imName) {
        this.imName = imName;
       return  saveSelf();
    }

    public LoginInfo setImPwd(String imPwd) {
        this.imPwd = imPwd;
        return  saveSelf();
    }

    public boolean isDoorImVaild(){
        return !(EmptyDeal.isEmpy(imName)||EmptyDeal.isEmpy(imPwd));
    }
    public boolean isValid() {
        return  !EmptyDeal.isEmpy(id);
    }
}
