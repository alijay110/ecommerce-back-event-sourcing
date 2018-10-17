package pl.cba.gibcode.monitoringComponent;

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
public class MonitoringComponentApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonitoringComponentApplication.class, args);
	}
}
