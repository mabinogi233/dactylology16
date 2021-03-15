package com.king.dactylology.LoginModule.Utils.UtilsItems;


import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class DateOpeartor {
    /**
     * 获取min分钟后的日期
     * @param nowDate
     * @param min
     * @return
     */
     Date addDateMin(Date nowDate, int min){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    /**
     * 获取hours小时后的日期
     * @param nowDate
     * @param hours
     * @return
     */
    Date addDateHour(Date nowDate,int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


}
