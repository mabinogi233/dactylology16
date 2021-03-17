package com.king.dactylology.ResourceOperator;


//300~399
public enum  Code {
    ResourceGetSuccess("资源查找成功","300"),
    ResourceNotFind("资源查找失败","301");

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
