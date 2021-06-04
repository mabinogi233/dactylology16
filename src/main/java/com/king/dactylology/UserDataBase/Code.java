package com.king.dactylology.UserDataBase;


//500~599
//用户昵称/头像相关
public enum Code {
    UpdateSuccess("修改成功","500"),
    GetSuccess("获取成功","501"),
    UpdateError("修改失败","502"),
    GetError("获取失败","503");

    String text;

    String code;

    private Code(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getText(){
        return text;
    }
}
