/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.webbudget.infraestructure.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.1.0, 07/03/2015
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config/webbudget-persistence.properties")
public class JPAConfiguration {

    @Autowired
    private Environment env;

    /**
     * 
     * @return 
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        final LocalContainerEntityManagerFactoryBean factoryBean
                = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(this.dataSource());
        factoryBean.setPersistenceUnitName("webBudgetPU");
        factoryBean.setPackagesToScan("br.com.webbudget.domain.entity");

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(additionalProperties());

        return factoryBean;
    }

    /**
     * 
     * @return 
     */
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        
        final org.apache.tomcat.jdbc.pool.DataSource dataSource = 
                new org.apache.tomcat.jdbc.pool.DataSource();
        
        // config default do DS
        dataSource.setDriverClassName(this.env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(this.env.getProperty("jdbc.url"));
        dataSource.setUsername(this.env.getProperty("jdbc.username"));
        dataSource.setPassword(this.env.getProperty("jdbc.password"));
        
        // config do pool
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(25);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(5);
        
        // desabilita o autocommit
        dataSource.setDefaultAutoCommit(false);
        
        return dataSource;
    }

    /**
     * 
     * @param factory
     * @return 
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory factory) {
        
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        
        transactionManager.setEntityManagerFactory(factory);

        return transactionManager;
    }

    /**
     * 
     * @return 
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     *
     * @return
     */
    private Properties additionalProperties() {

        final Properties properties = new Properties();

        // opcoes do hibernate
        properties.setProperty("hibernate.show_sql", this.env.getProperty("jpa.showSql"));
        properties.setProperty("hibernate.dialect", this.env.getProperty("jpa.databaseDialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", this.env.getProperty("jpa.generateDdl"));
        
        // desabilita JSR-303 no insert do banco
        properties.setProperty("javax.persistence.validation.mode", "none");

        return properties;
    }
}
