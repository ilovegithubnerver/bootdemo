package com.shiyi.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;



import javax.sql.DataSource;

/**
 * 事务配置
 * @author chenjiangpeng
 * @date
 */
@Configuration
// 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {

    // 其中 dataSource 框架会自动为我们注入
    @Autowired
    DataSource dataSource;

    Logger logger= LoggerFactory.getLogger(MyBatisConfig.class);



    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {

        // bean.setTypeAliasesPackage("tk.mybatis.springboot.model");
        //分页插件
        //PageHelper pageHelper = new PageHelper();
        //Properties properties = new Properties();
        //properties.setProperty("reasonable", "true");
        //properties.setProperty("supportMethodsArguments", "true");
        //properties.setProperty("returnPageInfo", "check");
        //properties.setProperty("params", "count=countSql");
        //pageHelper.setProperties(properties);
        //添加插件
        //   bean.setPlugins(new Interceptor[]{pageHelper});
        //  bean.set
        //添加XML目录
        //  ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource mybatisConfigXml = resolver.getResource("classpath:/mybatis/mybatis-config.xml");
            bean.setConfigLocation(mybatisConfigXml);
            bean.setMapperLocations(resolver.getResources("classpath:/mybatis/mapper/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(""+e);
            throw new RuntimeException(e);
        }
    }

    //@Resource(value="txManager2")
    //private PlatformTransactionManager txManager2;
    //// 创建事务管理器2
    //@Bean(name = "txManager2")
    //public PlatformTransactionManager txManager2(EntityManagerFactory factory) {
    //    return new JpaTransactionManager(factory);
    //}



    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     *   实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
     * @return
     */
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }


}