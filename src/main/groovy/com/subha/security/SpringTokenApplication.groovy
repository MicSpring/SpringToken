package com.subha.security.properties

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@ComponentScan(basePackages = ["org.springframework.boot.autoconfigure.jdbc","com.subha"])
class SpringTokenApplication {

    static main(args) {
        new SpringApplicationBuilder(SpringTokenApplication.class)
        				.run(args);

    }
}