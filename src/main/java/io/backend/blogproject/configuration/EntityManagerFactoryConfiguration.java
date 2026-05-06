package io.backend.blogproject.configuration;

import io.backend.blogproject.domain.entity.Category;
import io.backend.blogproject.domain.entity.Comment;
import io.backend.blogproject.domain.entity.Post;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class EntityManagerFactoryConfiguration {
	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;


	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory() {

		HibernatePersistenceConfiguration hpc = new HibernatePersistenceConfiguration("blogproject-hibernate")
			.jdbcDriver(driverClassName)
			.jdbcUrl(url)
			.jdbcUsername(username)
			.jdbcPassword("")
			.managedClass(Comment.class)
			.managedClass(Post.class)
			.managedClass(Category.class)

			.property("hibernate.hbm2ddl.auto", "update");

		return hpc.createEntityManagerFactory();
	}
}
