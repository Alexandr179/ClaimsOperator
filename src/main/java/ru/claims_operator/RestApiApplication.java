package ru.claims_operator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.claims_operator.utils.TestPopulatedDataUtil;

@SpringBootApplication
public class RestApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RestApiApplication.class, args);

        TestPopulatedDataUtil testPopulateDBDataUtil = context.getBean(TestPopulatedDataUtil.class);
        testPopulateDBDataUtil.initializeData();// test Data init with hibernate
    }
}
