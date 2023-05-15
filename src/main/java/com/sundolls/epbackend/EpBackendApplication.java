package com.sundolls.epbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@PropertySource(value = { "classpath:database.properties"})
@PropertySource(value = { "classpath:oauth.properties"})
@PropertySource(value = { "classpath:jwt.properties"})
public class EpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpBackendApplication.class, args);
    }

}
