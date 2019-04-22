package com.myBusiness.products.restaurant.entity;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.vo.OrderGoodsInfoVo;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

public class TestGson {

    @Test
    public void object2Json(){
        Gson gson = new Gson();

        OrderGoodsInfoVo orderGoodsInfo = new OrderGoodsInfoVo();
        orderGoodsInfo.setGoodsId(1);
        orderGoodsInfo.setAmount(50);
        orderGoodsInfo.setOriginalPrice(CommonUtil.setRoundHalfUpKeep2DecimalPlaces(new BigDecimal(3.3555)));
        orderGoodsInfo.setRealPrice(CommonUtil.setRoundHalfUpKeep2DecimalPlaces(new BigDecimal(5000)));

        List<OrderGoodsInfoVo> list = new ArrayList<>();
        list.add(orderGoodsInfo);
        list.add(orderGoodsInfo);
        list.add(orderGoodsInfo);

        String s = gson.toJson(list);
        System.out.println(s);
        List list1 = gson.fromJson(s, List.class);
        System.out.println(list1);
    }

    @Test
    public void test(){
        BigDecimal bigDecimal = new BigDecimal(3.3);
        BigDecimal bigDecimal1 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(bigDecimal);
        System.out.println(bigDecimal1);

    }

    @Test
    public void test1() throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(0.1);
        Thread.sleep(1000 * 2);
        for(int i=0;i<20;i++){
            System.out.println(i+":"+limiter.tryAcquire(2,TimeUnit.SECONDS));
//            System.out.println(i+":"+limiter.tryAcquire(2,TimeUnit.SECONDS))
        }
    }

    @Test
    public void tesBigDecimal(){
        BigDecimal bigDecimal1 = new BigDecimal("20");
        BigDecimal bigDecimal2 = new BigDecimal("8.5");
        BigDecimal divide = bigDecimal1.multiply(bigDecimal2).divide(new BigDecimal(10));
        BigDecimal bigDecimal = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(divide);
        System.out.println(bigDecimal.toString());
    }

    @Test
    public void tesBigDecimal2(){
        BigDecimal bigDecimal1 = new BigDecimal("20");
        BigDecimal bigDecimal2 = new BigDecimal("8.5");
        bigDecimal1.add(bigDecimal2);
        System.out.println(bigDecimal1.toString());
    }


    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


    private static Random random = new Random();


    @Test
    public void genOrderId(){
        String dateTimeStr = df.format(LocalDateTime.now());
        long dateTimeLong = Long.parseLong(dateTimeStr);
        int randomNum = random.nextInt(8999) + 1000;
        long id =  (dateTimeLong * 10000 + randomNum) * 10 ;
        System.out.println(id);
    }

    @Test
    public void testCopy(){
        String s = "ABCDEF";
        byte[] bytes = s.getBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        byte[] temp = new byte[0];
        bis.skip(6);
//        bis.reset();
        int read = bis.read(temp, 0, 0);
        bytes = null;
        System.out.println(read);
        System.out.println(new String(temp));
    }

    @Test
    public void testDoWhile(){
        int i=8;
        do{
            i++;
            System.out.println(i);
        }while(i<10);
    }

    @Test
    public void testcc(){
        int splitSize = 13;
        byte[] bytes = "ABBBCCDEFABBCBCDCDEEFABFAEFADCDECDCDEFABEFABFADEFACDEF".getBytes();

        List<byte[]> list = new ArrayList<>();
        int length = bytes.length;
        int size = length / splitSize;
        int mod = Math.floorMod(length,splitSize) ;
        byte[] subBytes = new byte[size>0?splitSize:mod];
        for (int i = 0; i < length; i++) {
            int floorMod = Math.floorMod(i, splitSize);
            subBytes[floorMod]= bytes[i];
            if(subBytes.length-1==floorMod){
                list.add(subBytes);
                size--;
                subBytes =  new byte[size>0?splitSize:mod];
            }
        }

        for(int i = 0 ; i < list.size() ; i ++){
            System.out.println(list.get(i).length + "," + new String(list.get(i)));
        }

        System.out.println("==============================");
        list = new ArrayList<>();

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        int read = 0;
        while(true){
            int tempLength = bis.available() > splitSize ? splitSize : bis.available();
            byte[] temp = new byte[tempLength];
            read = bis.read(temp, 0, tempLength);
            if(read == -1){
                break;
            }
            list.add(temp);
        };

        for(int i = 0 ; i < list.size() ; i ++){
            System.out.println(list.get(i).length + "," + new String(list.get(i)));
        }
    }

    @Test
    public void testInt(){
        String str = "{\"1\":\"b\"}";
        Gson gson = new Gson();
        Map<Integer,Integer> map = gson.fromJson(str,new TypeToken<Map<Integer,Integer>>(){}.getType());
        System.out.println(map.get(1));
    }

    @Test
    public void testGson1(){
        Gson gson = new Gson();
        String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
        String[] strings = gson.fromJson(jsonArray, String[].class);
        List<String> stringList = gson.fromJson(jsonArray,
                new TypeToken<List<String>>() {

                }.getType());

    }

    @Test
    public void testGson2() throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
        writer.beginObject()// throws IOException
        .name("name").value("怪盗kidou")
                .name("age").value(24)
                .name("email").nullValue() //演示null
        .endObject(); // throws IOException
        writer.flush(); // throws IOException //{"name":"怪盗kidou","age":24,"email":null

    }

    @Test
    public void testRegex(){
        Pattern ppp = Pattern.compile("ppp");
        String str = "123ppp456";

        boolean r1 = str.matches(".*ppp.*");
        System.out.println("r1：" + r1);

        boolean r2 = str.matches("ppp");
        System.out.println("r2：" + r2);

        boolean r3 = Pattern.matches(".*ppp.*", str);
        System.out.println("r3：" + r3);

        boolean r4 = Pattern.matches("ppp", str);
        System.out.println("r4：" + r4);

    }
}
