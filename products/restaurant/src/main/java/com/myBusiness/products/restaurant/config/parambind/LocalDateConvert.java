package com.myBusiness.products.restaurant.config.parambind;

import com.myBusiness.common.util.CommonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//@Configuration
public class LocalDateConvert implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String s) {
        if(CommonUtil.isEmpty(s)){
            return null;
        }
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(s, DateTimeFormatter.ISO_DATE);
        }catch (DateTimeParseException e){
            String message = "日期格式不正确，格式必须为yyyy-MM-dd";
            throw new ConstraintViolationException(message,null);
        }
        return localDate;
    }
}
