package pl.cba.gibcode.cardComponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan
@Configuration
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
public class CardComponentApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardComponentApplication.class, args);
	}
}
