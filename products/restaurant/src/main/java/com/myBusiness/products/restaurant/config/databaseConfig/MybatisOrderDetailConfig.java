package com.myBusiness.products.restaurant.config.databaseConfig;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import io.shardingsphere.shardingjdbc.spring.boot.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import io.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@AutoConfigureAfter({CommonConfig.class})
@Configuration
@MapperScan(basePackages = "com.myBusiness.products.restaurant.dao.orderDetail",sqlSessionFactoryRef = "orderDetailSqlSessionFactory")
@EnableConfigurationProperties({SpringBootShardingRuleConfigurationProperties.class, SpringBootMasterSlaveRuleConfigurationProperties.class})
public class MybatisOrderDetailConfig {

    @Autowired
    private SpringBootShardingRuleConfigurationProperties shardingProperties;

    @Autowired
    private SpringBootMasterSlaveRuleConfigurationProperties masterSlaveProperties;

    private PathMatchingResourcePatternResolver resolver;

    private Environment environment;

    public MybatisOrderDetailConfig(Environment environment,PathMatchingResourcePatternResolver resolver){
        this.environment = environment;
        this.resolver = resolver;
    }

    /**
     * 订单明细数据源
     * @return
     */
    @Bean(name = "orderdetaildatasource")
    @ConfigurationProperties(prefix = "mybusiness.datasource.orderdetail")
    public DataSource orderDetailDatasource()  {
        return DataSourceBuilder.create().build();
    }

    /**
     * 以mybusinessorderdetail为key，dataSource为value创建map
     * 参照 io.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration
     * 这一步目的是将原始DataSource和我们在配置文件中的分片策略对应起来
     * 注意key要和shardingJdbc配置项中的一致
     * @return
     * @throws SQLException
     */
    @Bean(name = "shardingDataSource")
    public DataSource shardingDataSource() throws SQLException {
        Map<String,DataSource> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("mybusinessorderdetail", orderDetailDatasource());
        return null == masterSlaveProperties.getMasterDataSourceName()
                ? ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingProperties.getShardingRuleConfiguration(), shardingProperties.getConfigMap(), shardingProperties.getProps())
                : MasterSlaveDataSourceFactory.createDataSource(
                dataSourceMap, masterSlaveProperties.getMasterSlaveRuleConfiguration(), masterSlaveProperties.getConfigMap(), masterSlaveProperties.getProps());
    }

    @Bean(name = "orderDetailDatasourceTransactionManager")
    public DataSourceTransactionManager orderDetailDatasourceTransactionManager(){
        return new DataSourceTransactionManager(orderDetailDatasource());
    }

    /**
     * 将shardingJdbc创建的DataSource传入这里的SqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "orderDetailSqlSessionFactory")
    public SqlSessionFactory orderDetailSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(shardingDataSource());
        sqlSessionFactory.setMapperLocations(resolveMapperLocations(environment.getProperty("mybusiness.orderdetail.mapper-locations").split(",")));
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
