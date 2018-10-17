package pl.cba.gibcode.apiQuery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class ApiQueryApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiQueryApplication.class, args);
	}

}
