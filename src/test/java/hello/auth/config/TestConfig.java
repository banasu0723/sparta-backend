package hello.auth.config;

import hello.auth.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public UserRepository testUserRepository() {
        return new UserRepository();
    }
    @Bean
    public PasswordEncoder testPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
