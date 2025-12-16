package cn.zm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class ResourceServer {
    public static void main(String[] args) {
        SpringApplication.run(ResourceServer.class, args);
    }
}