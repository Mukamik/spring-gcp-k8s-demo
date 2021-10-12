package com.wanjala.gcpdemo;

import com.google.common.base.Preconditions;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
    em.setPackagesToScan("com.wanjala.gcpdemo.repository");
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    return em;
  }

  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_URL")));
    dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_USERNAME")));
    dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_PASSWORD")));
    return dataSource;
  }


}
