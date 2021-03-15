package com.king.dactylology.fileUpLoad;

//100~199
//文件上传组件的状态码
public enum Code {
    UpFileSuccess("上传文件成功","100"),
    UpFileFail("上传文件失败","101");

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
