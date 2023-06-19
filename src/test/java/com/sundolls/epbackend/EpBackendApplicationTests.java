package com.sundolls.epbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@PropertySource(value = { "classpath:database.properties"})
@PropertySource(value = { "classpath:oauth.properties"})
@PropertySource(value = { "classpath:jwt.properties"})
class EpBackendApplicationTests {

    @Test
    void contextLoads() {

    }

}
