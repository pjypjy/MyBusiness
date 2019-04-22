package com.myBusiness.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class IdUtil {

    private static Random random = new Random();

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 订单id
     * @return
     */
    public static synchronized long genOrderId(){
        String dateTimeStr = df.format(LocalDateTime.now());
        long dateTimeLong = Long.parseLong(dateTimeStr);
        int randomNum = random.nextInt(8999) + 1000;
        return (dateTimeLong * 10000 + randomNum) * 10 ;
    }

    /**
     * 支付记录id
     * @return
     */
    public static synchronized long genPayRecordId(){
        String dateTimeStr = df.format(LocalDateTime.now());
        long dateTimeLong = Long.parseLong(dateTimeStr);
        int randomNum = random.nextInt(8999) + 1000;
        return (dateTimeLong * 10000 + randomNum) * 10 + 9 ;
    }

    /**
     * 用户id
     * @return
     */
    public static synchronized long genUserId(){
        return System.currentTimeMillis() ;
    }

    /**
     * 产生菜单id
     * @return
     */
    public static synchronized long genMenuBarId(){
        return System.currentTimeMillis() ;
    }

    /**
     * 产生角色id
     * @return
     */
    public static synchronized long genRoleId(){
        return System.currentTimeMillis() ;
    }

    /**
     * 产生优惠券id
     * @return
     */
    public static synchronized long genCouponId(){
        return System.currentTimeMillis() ;
    }

    public static long genCancelId(long orderId){
        return orderId * 1000 + 500;
    }
}
