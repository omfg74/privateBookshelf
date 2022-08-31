package com.omfgdevelop.privatebookshelf


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.testcontainers.containers.PostgreSQLContainer

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import javax.sql.DataSource
import java.time.Duration

/**
 * TestContainers config
 */
@Configuration
@ComponentScan(basePackages = ["com.omfgdevelop.privatebookshelf"])
class DbConfigGroovy {


    @Value('${spring.datasource.url}')
    String datasourceUrl

    @Value('${spring.datasource.username}')
    String username

    @Value('${spring.datasource.password}')
    String password

    private String getDatabase() {
        final URI uri = new URI(datasourceUrl.replace("jdbc:", ""))
        return uri.getPath().substring(1)
    }

    @Bean
    PostgreSQLContainer postgreSqlContainer() {
        def migrationsDir = new File("src/test/resources/sql")

        def postgreSQLContainer = (new PostgreSQLContainer("postgres:14")
                .withDatabaseName(getDatabase())
                .withUsername(username)
                .withPassword(password)
                .withFileSystemBind(migrationsDir.getAbsolutePath(), "/docker-entrypoint-initdb.d")
                .withStartupTimeout(Duration.ofSeconds(600))) as PostgreSQLContainer

        postgreSQLContainer.start()
        postgreSQLContainer
    }


    @Bean
    DataSource dataSource() {
        def ds = new DriverManagerDataSource()
        ds.setDriverClassName("org.postgresql.Driver")

        ds.setUrl(String.format("jdbc:postgresql://%s:%s/%s",
                postgreSqlContainer().getContainerIpAddress(),
                postgreSqlContainer().getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                getDatabase()))
        ds.setUsername(username)
        ds.setPassword(username)

        ds
    }


    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        def managerFactoryBean = new LocalContainerEntityManagerFactoryBean()
        managerFactoryBean.setDataSource(dataSource)

        managerFactoryBean.setPackagesToScan("com.omfgdevelop.privatebookshelf")

        def va = new HibernateJpaVendorAdapter()

        managerFactoryBean.setJpaVendorAdapter(va)
        managerFactoryBean.afterPropertiesSet()

        managerFactoryBean
    }

}
