package pl.cba.gibcode.apiCommand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class ApiCommandApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiCommandApplication.class, args);
	}

}
