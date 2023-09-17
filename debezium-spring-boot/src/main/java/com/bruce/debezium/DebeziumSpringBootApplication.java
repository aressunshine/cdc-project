package com.bruce.debezium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class DebeziumSpringBootApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(DebeziumSpringBootApplication.class, args);

        Environment environment = context.getBean(Environment.class);

        String ip = InetAddress.getLocalHost().getHostAddress();
        String serverPort = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");

        String actuatorPort = environment.getProperty("management.server.port");
        String actuatorPath = environment.getProperty("management.endpoints.web.base-path");

        System.out.println(
                "----------------------------------------------------------\n" +
                        "DebeziumSpringBootApplication is running! Access URL:\n" +
                        "Local: \t\thttp://localhost:" + serverPort + contextPath + "\n" +
                        "Actuator: \thttp://localhost:" + actuatorPort + actuatorPath + "\n" +
                        "----------------------------------------------------------");
    }

}
