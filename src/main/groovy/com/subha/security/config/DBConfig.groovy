package com.subha.security.config

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
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

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate()
    {
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client())
        elasticsearchTemplate
    }

    @Bean
    public Client client() {
        Settings settings = Settings.settingsBuilder().put("cluster.name",'elasticsearch').put("client.transport.sniff",true).build()
        TransportClient transportClient =  TransportClient.builder().settings(settings).build()

        InetAddress inetAddress =  InetAddress.getByName("127.0.0.1")
        transportClient.addTransportAddress(new InetSocketTransportAddress(inetAddress, 9300))
        transportClient
    }

}
