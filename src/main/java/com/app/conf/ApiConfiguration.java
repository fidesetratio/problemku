package com.app.conf;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ApiConfiguration {
	@Autowired
	private Environment env;
	
	
	@Bean
	@Primary
	public SqlSessionFactoryBean sqlSessionFactoryBean() {
	    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	    sqlSessionFactoryBean.setDataSource(dataSource());
	    sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("sqlmapconfig.xml"));
	    
	    return sqlSessionFactoryBean;
	}
	
	
	@Bean
	@Primary
	public DataSource dataSource() {
		// TODO Auto-generated method stub
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		return dataSource;
	}



	  @Bean
	  @Primary
	  public SqlSessionTemplate sqlSession(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
	    return new SqlSessionTemplate(sqlSessionFactoryBean.getObject(),ExecutorType.BATCH);
	  }



	  /***
	   * PROD FOR TESTING ONLY
	   */



	    @Bean(name = "prod")
		public DataSource dataSourceProd() {
			// TODO Auto-generated method stub
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(env.getProperty("spring.datasource.prod.driverClassName"));
			dataSource.setUrl(env.getProperty("spring.datasource.prod.url"));
			dataSource.setUsername(env.getProperty("spring.datasource.prod.username"));
			dataSource.setPassword(env.getProperty("spring.datasource.prod.password"));
			return dataSource;
		}

		@Bean(name = "sqlSessionFactProd")
		public SqlSessionFactoryBean sqlSessionFactoryBeanProd(@Qualifier("prod") DataSource dataSource) {
			SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
			sqlSessionFactoryBean.setDataSource(dataSource);
			sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("sqlmapconfigprod.xml"));
			return sqlSessionFactoryBean;
		}

		@Bean(name = "sqlSessionProd")
		public SqlSessionTemplate sqlSessionProd(
				@Qualifier("sqlSessionFactProd") SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
			return new SqlSessionTemplate(sqlSessionFactoryBean.getObject(), ExecutorType.BATCH);
		}





}
