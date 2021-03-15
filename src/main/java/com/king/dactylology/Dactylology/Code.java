package com.king.dactylology.Dactylology;

//200~299
//枚举，存储手语翻译的code状态码
public enum Code {
    TransforSuccess("翻译成功","200"),
    TransforError("翻译失败","201");

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
