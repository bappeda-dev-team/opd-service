package kk.kertaskerja.opdservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OpdServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpdServiceApplication.class, args);
    }
}
