package com.subha.security.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder

import javax.sql.DataSource

/**
 * Created by user on 8/4/2016.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.subha.security.repo")
class DBConfig {

    @Autowired
    EmbeddedDataSourceConfiguration embeddedDataSourceConfiguration

    @Bean
    public DataSource dataSource() {

        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = embeddedDataSourceConfiguration.dataSource()
               /* builder
                .setType(EmbeddedDatabaseType.HSQL) //.H2 or .DERBY
                .addScript("db/sql/create-db.sql")
                .addScript("db/sql/insert-data.sql")
                .build();*/
        return db;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        def jdbcTemplate = new JdbcTemplate(dataSource: dataSource())
        jdbcTemplate
    }


}
