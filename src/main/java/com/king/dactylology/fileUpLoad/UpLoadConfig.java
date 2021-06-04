package com.king.dactylology.fileUpLoad;

import com.king.dactylology.fileUpLoad.Utils.MyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

//配置类，配置监听器和mutipart协议拦截器
@Configuration
public class UpLoadConfig {

    //储存空间根目录
    public static String rootPath = "D:\\试验田\\javaproject";

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        return new MyResolver();
    }
}
