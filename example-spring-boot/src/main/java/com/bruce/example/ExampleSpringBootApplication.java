package com.bruce.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ExampleSpringBootApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(ExampleSpringBootApplication.class, args);

        Environment environment = context.getBean(Environment.class);

        String ip = InetAddress.getLocalHost().getHostAddress();
        String serverPort = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");

        String actuatorPort = environment.getProperty("management.server.port");
        String actuatorPath = environment.getProperty("management.endpoints.web.base-path");

        System.out.println(
                "----------------------------------------------------------\n" +
                        "ExampleSpringBootApplication is running! Access URL:\n" +
                        "Local: \t\thttp://localhost:" + serverPort + contextPath + "\n" +
                        "External: \thttp://" + ip + ":" + serverPort + contextPath + "\n" +
                        "Swagger2: \thttp://localhost:" + serverPort + contextPath + "doc.html\n" +
                        "Actuator: \thttp://localhost:" + actuatorPort + actuatorPath + "\n" +
                        "----------------------------------------------------------");
    }

}
