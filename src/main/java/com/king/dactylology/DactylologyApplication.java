package com.king.dactylology;

import com.king.dactylology.LoginModule.Utils.UtilsItems.GetBeans;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan({"com.king.dactylology.LoginModule.Utils.Dao.Mapper","com.king.dactylology.ResourceOperator.Dao.Mapper"})

public class DactylologyApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(DactylologyApplication.class, args);
        //将run方法的返回值赋值给工具类中的静态变量
        //配置GetBeans类
        GetBeans.applicationContext = applicationContext;
    }

}
