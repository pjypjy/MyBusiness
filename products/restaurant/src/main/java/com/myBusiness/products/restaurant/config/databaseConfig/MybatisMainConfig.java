package com.myBusiness.products.restaurant.config.databaseConfig;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@AutoConfigureAfter({CommonConfig.class})
@Configuration
@MapperScan(basePackages = "com.myBusiness.products.restaurant.dao.main",sqlSessionFactoryRef = "mainSqlSessionFactory")
public class MybatisMainConfig {

    private PathMatchingResourcePatternResolver resolver;

    private Environment environment;

    public MybatisMainConfig(Environment environment,PathMatchingResourcePatternResolver resolver){
        this.environment = environment;
        this.resolver = resolver;
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean(name = "mainDatasource")
    @ConfigurationProperties(prefix = "mybusiness.datasource.main")
    public DataSource mainDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mainJdbcTemplate")
    public JdbcTemplate mainJdbcTemplate(){
        return new JdbcTemplate(mainDatasource());
    }

    @Bean(name = "mainDatasourceTransactionManager")
    public DataSourceTransactionManager mainDatasourceTransactionManager(){
        return new DataSourceTransactionManager(mainDatasource());
    }

    @Bean(name = "mainSqlSessionFactory")
    public SqlSessionFactory mainSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(mainDatasource());
        sqlSessionFactory.setMapperLocations(resolveMapperLocations(environment.getProperty("mybusiness.main.mapper-locations").split(",")));
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(new GlobalConfig.DbConfig().setDbType(DbType.MYSQL));
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }

    public Resource[] resolveMapperLocations(String[] locations) {
        return Stream.of(Optional.ofNullable(locations).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location)))
                .toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }
}
