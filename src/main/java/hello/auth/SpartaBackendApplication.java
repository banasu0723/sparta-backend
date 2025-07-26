package hello.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hello.auth")
public class SpartaBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpartaBackendApplication.class, args);
	}

}
