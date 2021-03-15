package com.king.dactylology.LoginModule.Utils.Dao.entity;

import java.util.Date;

public class token {
    private Integer id;

    private String token;

    private Date tokendeadtime;

    private Date tokengettime;

    private Date refreshtokendeadtime;

    private String refreshtoken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getTokengettime() {
        return tokengettime;
    }

    public void setTokengettime(Date tokengettime) {
        this.tokengettime = tokengettime;
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