package com.king.dactylology.LoginModule.Utils.Dao.entity;

import java.util.Date;

public class oauthToken {
    private Integer id;

    private String oauthname;

    private String token;

    private Date tokendeadtime;

    private Date refreshtokendeadtime;

    private String refreshtoken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOauthname() {
        return oauthname;
    }

    public void setOauthname(String oauthname) {
        this.oauthname = oauthname == null ? null : oauthname.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getTokendeadtime() {
        return tokendeadtime;
    }

    public void setTokendeadtime(Date tokendeadtime) {
        this.tokendeadtime = tokendeadtime;
    }

    public Date getRefreshtokendeadtime() {
        return refreshtokendeadtime;
    }

    public void setRefreshtokendeadtime(Date refreshtokendeadtime) {
        this.refreshtokendeadtime = refreshtokendeadtime;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken == null ? null : refreshtoken.trim();
    }
}