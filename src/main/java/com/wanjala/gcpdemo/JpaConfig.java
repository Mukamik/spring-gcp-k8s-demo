package com.wanjala.gcpdemo;

import com.google.common.base.Preconditions;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@ComponentScan("com.wanjala.gcpdemo")
@EnableJpaRepositories("com.wanjala.gcpdemo.repository")
public class JpaConfig {

  @Autowired
  private Environment env;

  public JpaConfig() {
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
    dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_DRIVER_CLASS_NAME")));
    dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_URL")));
    dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_USERNAME")));
    dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("SPRING_DATASOURCE_PASSWORD")));
    return dataSource;
  }


}
