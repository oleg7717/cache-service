package ru.interprocom.axioma.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.interprocom.axioma.cache.repository.RefreshRepositoryImpl;

@EnableJpaAuditing
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = RefreshRepositoryImpl.class)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
