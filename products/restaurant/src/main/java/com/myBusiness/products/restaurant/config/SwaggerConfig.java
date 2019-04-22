package com.myBusiness.products.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String presentActive;

    @Bean
    public Docket createACCESSRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("token").description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        if("tencent".equals(presentActive)){
            return new Docket(DocumentationType.SWAGGER_2)
                    .host("139.199.171.136")
                    .apiInfo(apiInfo()).groupName("餐厅服务")
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.myBusiness.products.restaurant.web"))
                    .paths(PathSelectors.any())
                    .build().globalOperationParameters(pars).apiInfo(apiInfo());
        }else {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo()).groupName("餐厅服务")
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.myBusiness.products.restaurant.web"))
                    .paths(PathSelectors.any())
                    .build().globalOperationParameters(pars).apiInfo(apiInfo());
        }


    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("餐厅服务")
                .description("餐厅服务....")
                .build();
    }
}
