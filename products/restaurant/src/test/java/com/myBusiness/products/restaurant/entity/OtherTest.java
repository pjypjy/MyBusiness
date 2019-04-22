package com.myBusiness.products.restaurant.entity;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeMap;

public class OtherTest {

    @Test
    public void test(){
        TreeMap<Integer,Boolean> doors = new TreeMap<>();

        //第一个人把100扇门关上
        for(int i = 1 ; i <=100 ; i++){
            doors.put(i,false);
        }
        for(int i = 2 ; i <=100 ; i++){
            Set<Integer> doorNumbers = doors.keySet();
            for(int doorNumber : doorNumbers){
                //第N个人把第doorNumber扇门和能被N整除的门  关上 或者 打开
                //doorNumber能被N整除
                if(doorNumber % i == 0){
                    //判断门是开还是关
                    if(doors.get(doorNumber)){
                        //关掉开着的门
                        doors.put(doorNumber,false);
                    }else{
                        //打开关闭的门
                        doors.put(doorNumber,true);
                    }
                }
            }
        }

        Set<Integer> doorNumbers = doors.keySet();
        for(int doorNumber : doorNumbers){
            System.out.println(doorNumber + ":" + doors.get(doorNumber));
        }
    }

    @Test
    public void test2(){
        TreeMap<Integer,Boolean> doors = new TreeMap<>();
        for(int i = 1 ; i <=100 ; i++){
            //约数个数
            int nums = 0;
            for(int j = 1 ; j <= i ; j++){
                if(i % j == 0){
                    //能整除，约数个数+1
                    nums ++;
                }
            }
            if(nums % 2 == 0){
                //如果约数个数是复数，门是开的
                doors.put(i,true);
            }else{
                //如果约数个数是奇数，门是关的
                doors.put(i,false);
            }
        }

        Set<Integer> doorNumbers = doors.keySet();
        for(int doorNumber : doorNumbers){
            System.out.println(doorNumber + ":" + doors.get(doorNumber));
        }
    }

    @Test
    public void testLocalDate(){
        System.out.println(LocalDate.now().equals(LocalDate.now()));
    }

    @Test
    public void testTryFinally(){
        int i = tryFinally();
//        new CouponEntity().clone();
        System.out.println(i);
    }

    public int tryFinally(){
        //return 2
        try {
            return 1;
        }finally {
            return 2;
        }
    }

}
