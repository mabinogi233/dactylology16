package com.king.dactylology.fileUpLoad.Utils;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


/**
 * 监听器，监听mutipart文件上传的进度
 */
@Component
public class MyListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session) {
        this.session = session;
        session.setAttribute("uploadPercent", 0);
    }

    @Override
    public void update(long l, long l1, int i) {
        //计算当前上传的百分比
        double percent = (double) ((double)l*100.0/(double)l1 );
        session.setAttribute("uploadPercent", percent);
    }
}

