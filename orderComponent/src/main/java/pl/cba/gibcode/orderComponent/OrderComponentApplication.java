package pl.cba.gibcode.orderComponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EntityScan
@Configuration
public class OrderComponentApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderComponentApplication.class, args);
	}
}
