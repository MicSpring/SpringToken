package com.subha.security.config;

import com.subha.security.properties.SpringTokenApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by user on 8/4/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringTokenApplication.class})
public class DBConfigTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String url;

    @Test
    public void testUrl() {
        System.out.println("The URL is:"+url);
    }

    @Test
    public void testDbConfig() {
        System.out.println("The JdbcTemplate is:"+jdbcTemplate);
        System.out.println(jdbcTemplate.queryForList("select * from student2"));
    }
}
