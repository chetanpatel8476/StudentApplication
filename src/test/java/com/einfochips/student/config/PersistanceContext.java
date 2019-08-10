package com.einfochips.student.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;


@Configuration
@PropertySource("classpath:application-test.properties")
@EnableTransactionManagement
public class PersistanceContext {
	protected static final String DATABASE_DRIVER = "db.driver";
	protected static final String DATABASE_PASSWORD = "db.password";
	protected static final String DATABASE_URL = "db.url";
	protected static final String DATABASE_USERNAME = "db.username";

	protected static final String DIALECT = "hibernate.dialect";

	protected static final String FORMAT_SQL = "hibernate.format_sql";

	protected static final String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

	protected static final String NAMIN_STRATAGY = "hibernate.ejb.naming_strategy";

	protected static final String SHOW_SQL = "hibernate.show_sql";

	protected static final String PACKAGE_TO_SCAN = "com.einfochips.student.repository";

	@Autowired
	private Environment environment;

	@Bean
	JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("com.einfochips.student.entity");

		Properties jpaProperties = new Properties();

		jpaProperties.put("hibernate.dialect", env.getRequiredProperty(DIALECT));

		jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty(HBM2DDL_AUTO));

		jpaProperties.put("hibernate.ejb.naming_strategy", env.getRequiredProperty(NAMIN_STRATAGY));

		jpaProperties.put("hibernate.show_sql", env.getRequiredProperty(SHOW_SQL));

		jpaProperties.put("hibernate.format_sql", env.getRequiredProperty(FORMAT_SQL));

		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}

	@Bean
	public DataSource dataSource() {
		BoneCPDataSource datasource = new BoneCPDataSource();

		datasource.setDriverClass(environment.getRequiredProperty(DATABASE_DRIVER));
		datasource.setJdbcUrl(environment.getRequiredProperty(DATABASE_URL));
		datasource.setUsername(environment.getRequiredProperty(DATABASE_USERNAME));
		datasource.setPassword(environment.getRequiredProperty(DATABASE_PASSWORD));

		return datasource;
	}

}
