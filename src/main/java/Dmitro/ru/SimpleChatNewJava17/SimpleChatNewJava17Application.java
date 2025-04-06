package Dmitro.ru.SimpleChatNewJava17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "Dmitro.ru.SimpleChatNewJava17.repository")
public class SimpleChatNewJava17Application {

	public static void main(String[] args) {
		SpringApplication.run(SimpleChatNewJava17Application.class, args);
	}

}
