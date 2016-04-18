package me.glux.omd.boot.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import me.glux.omd.core.LongIdGenerator;
import me.glux.omd.dao.Mapper;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages={"me.glux.omd.dao"},annotationClass=Mapper.class)
public class MyBatisConfig {
    
    @Bean
    public LongIdGenerator idg(){
        return new LongIdGenerator();
    }

    @Bean(destroyMethod="close")
    @Primary
    @ConfigurationProperties(prefix = "c3p0")
    public ComboPooledDataSource dataSouce() {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setAcquireRetryDelay(1000);
        ds.setAcquireRetryAttempts(3);
        ds.setBreakAfterAcquireFailure(false);
        return ds;
    }
    
    @Bean
    @Autowired
    public PlatformTransactionManager txManager(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
    
    @Bean(name="sqlSessionFactory")
    @Autowired
    public SqlSessionFactoryBean sessionFactory(DataSource ds){
        SqlSessionFactoryBean sqlSessionFactory=new SqlSessionFactoryBean();
        sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactory.setDataSource(ds);
        return sqlSessionFactory;
    }
}
