package com.myBusiness.products.restaurant.config.parambind;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 　　4.2 定义一个注解接口实现类
 * 　　　　技巧01：必须实现ConstraintValidator接口
 * 　　　　技巧02：实现了ConstraintValidator接口后即使不进行Bean配置，spring也会将这个类进行Bean管理
 * 　　　　技巧03：可以在实现了ConstraintValidator接口的类中依赖注入其它Bean
 * 　　　　技巧04：实现了ConstraintValidator接口后必须重写 initialize 和 isValid 这两个方法；
 *                  initialize方法主要来进行初始化，通常用来获取自定义注解的属性值；
 *                  isValid 方法主要进行校验逻辑，返回true表示校验通过，返回false表示校验失败，
 *                             通常根据注解属性值和实体类属性值进行校验判断
 */
@Constraint(validatedBy = LocalDateValid.LocalDateValidChecker.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface LocalDateValid {

    boolean canEmpty() default false ;

    String message() default "参数错误";

    class LocalDateValidChecker implements ConstraintValidator<LocalDateValid,LocalDate> {

        boolean canEmpty;

        String message;

        @Override
        public void initialize(LocalDateValid constraintAnnotation) {
            this.canEmpty = constraintAnnotation.canEmpty();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
            if(canEmpty && value == null){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
            return true;
        }
    }

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
