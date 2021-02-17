package com.agaram.eln.secondary.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(basePackages = "com.agaram.eln.secondary.repository.archive",
        entityManagerFactoryRef = "archiveEntityManagerFactory",
        transactionManagerRef= "archiveTransactionManager")
public class ArchieveDataSourceConfiguration {
	
	@Autowired
    private Environment env;
	
	@Bean
	@ConfigurationProperties("app.datasource.archive")
	public DataSourceProperties archieveDataSourceProperties() {
	    return new DataSourceProperties();
	}
	@Bean
	@ConfigurationProperties("app.datasource.archive.configuration")
	public DataSource archiveDataSource() {
	    return archieveDataSourceProperties().initializeDataSourceBuilder()
	            .type(BasicDataSource.class).build();
	}
	
	@Bean(name = "archiveEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean archiveEntityManagerFactory(
			EntityManagerFactoryBuilder builder) {
		
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(archiveDataSource());
        factory.setPackagesToScan(new String[]{"com.agaram.eln.secondary.model.archive"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
     
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        //jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.hibernate.dialect"));
        jpaProperties.put("hibernate.connection.useUnicode", true);
        jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
        factory.setJpaProperties(jpaProperties);
     
        return factory;
//	        return builder.dataSource(archiveDataSource())
//	                .packages("com.agaram.eln.model.archieve")
//	                .build();
	}

	@Bean(name="archiveTransactionManager")
	public PlatformTransactionManager archiveTransactionManager(
	            final @Qualifier("archiveEntityManagerFactory") LocalContainerEntityManagerFactoryBean archiveEntityManagerFactory) {
	        return new JpaTransactionManager(archiveEntityManagerFactory.getObject());
	}

}
