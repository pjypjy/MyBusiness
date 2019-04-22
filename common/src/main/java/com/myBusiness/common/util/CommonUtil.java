package com.myBusiness.common.util;

import com.google.gson.Gson;
import com.myBusiness.common.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CommonUtil {

    protected final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    protected static final Gson gson = new Gson();

    protected static Random random = new Random();

    public static Map json2Map(String jsonStr){
        Map map = gson.fromJson(jsonStr, Map.class);
        return map;
    }

    public static <T> T fromJson(String jsonStr,Type typeOfT){
        return (T) gson.fromJson(jsonStr, typeOfT);
    }

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static boolean isEmpty(String str){
        if(str == null || str.trim().length() == 0){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str){
        if(str == null || str.trim().length() == 0){
            return false;
        }
        return true;
    }

    public static void returnJsonResponse(ResponseResult responseResult, HttpServletResponse response){
        try {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(gson.toJson(responseResult));
        }catch (Exception e){
            logger.error("写出json数据异常",e);
        }
    }

    /**
     * MD5加密
     */
    public static String encodeMD5(String s) {
        if (s == null || s.trim().length() == 0) {
            return null;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        md.update(s.getBytes());
        byte[] datas = md.digest();
        int len = datas.length;
        char str[] = new char[len * 2];
        int k = 0;
        for (byte byte0 : datas) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 随机生成 0-bound 范围内的数字,bound
     * 注意:不要超过int的范围
     * @param bound
     * @return
     */
    public static int nextRandomInt(int bound){
        return random.nextInt(bound);
    }

    public static String randomPasswordSuffix(){
        return String.valueOf(nextRandomInt(999));
    }

    /**
     * 重命名文件名
     * @param oldFileNameWithFormat 原始带后缀的文件名
     * @param newFileNameWithOutFormat 新的不带后缀的文件名
     * @return
     */
    public static String renameFile(String oldFileNameWithFormat,String newFileNameWithOutFormat){
        String formatWithPoint = oldFileNameWithFormat.substring(oldFileNameWithFormat.lastIndexOf("."));
        return newFileNameWithOutFormat + formatWithPoint;
    }

    /**
     * 产生商品图片的url
     * 需配合nginx使用
     * @param imgFileName 文件名，例如：default.jpg
     * @return 例如 http://139.199.171.136/restaurant/goods/img/default.jpg
     */
    public static String genGoodsImgUrl(String imgFileName){
        return StringPool.GOODS_IMG_BASE_URL.concat(imgFileName);
    }

    public static boolean isImg(String imgFileName){
        return imgFileName.endsWith(StringPool.GIF) || imgFileName.endsWith(StringPool.PNG) || imgFileName.endsWith(StringPool.JPEG) || imgFileName.endsWith(StringPool.JPG) || imgFileName.endsWith(StringPool.BMP);
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getTokenString() {
        return getHttpServletRequest().getHeader("token");
    }

    /**
     * 四舍五入保留2位小数
     * @param bigDecimal
     */
    public static BigDecimal setRoundHalfUpKeep2DecimalPlaces(BigDecimal bigDecimal){
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

    /**
     * 对象转json字符串
     * @param object
     * @return
     */
    public static String object2JsonString(Object object){
        return gson.toJson(object);
    }

    /**
     * json字符串转对象
     * @param jsonString
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T jsonString2Object(String jsonString,Class<T> classOfT){
        return gson.fromJson(jsonString,classOfT);
    }


}
