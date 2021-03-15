package com.king.dactylology.LoginModule.Utils.UtilsItems;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

/**
 * 根据bean的名称从spring容器中获取bean的一个实例化对象
 */
@Component
public class GetBeans extends ApplicationObjectSupport {
    public static ApplicationContext applicationContext = null;

    /**
     * 配置
     * @param context
     * @throws BeansException
     */
    @Override
    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        // TODO Auto-generated method stub
        super.initApplicationContext(context);
        if (GetBeans.applicationContext == null) {
            GetBeans.applicationContext = context;

        }
    }

    /**
     * 获取容器上下文
     * @return
     */
    private static ApplicationContext getAppContext() {
        return applicationContext;
    }

    /**
     * 根据bean的name获取实例对象
     * @param name
     * @return
     */
    static Object getBean(String name) {
        return getAppContext().getBean(name);
    }

    /**
     * 根据bean的类对象获取bean的实例对象
     * @param clazz
     * @return
     */
    static Object getBean(Class<?> clazz) {
        return getAppContext().getBean(clazz);
    }
}
