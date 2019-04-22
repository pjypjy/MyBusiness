package com.myBusiness.products.restaurant.config;

import com.myBusiness.common.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseResult constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        if(set == null || set.size() == 0){
            return ResponseResult.error(e.getMessage());
        }
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : set) {
            message.append(constraintViolation.getMessage());
        }
        return ResponseResult.error(message.toString());
    }
}
