package com.wanjala.gcpdemo;

import com.google.common.base.Preconditions;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"com.wanjala.gcpdemo.repositories"})
@EnableTransactionManagement
public class PersistenceConfig {

  @Autowired
  private Environment env;

  public PersistenceConfig() {
    super();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("com.wanjala.gcpdemo.models");
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());

    return em;
  }

  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_DRIVER_CLASS_NAME")));
    dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_URL")));
    dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_USERNAME")));
    dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_PASSWORD")));
    return dataSource;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  final Properties additionalProperties() {
    final Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.hbm2ddl.auto", Preconditions.checkNotNull(env.getProperty("HIBERNATE.HBM2DDL.AUTO")));
    hibernateProperties.setProperty("hibernate.dialect", Preconditions.checkNotNull(env.getProperty("HIBERNATE.DIALECT")));

    return hibernateProperties;
  }


}
