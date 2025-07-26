package hello.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // /docs → /swagger-ui/index.html 리다이렉션
        registry.addRedirectViewController("/docs", "/swagger-ui/index.html");

        // /swagger-ui/ → /swagger-ui/index.html 리다이렉션
        registry.addRedirectViewController("/swagger-ui/", "/swagger-ui/index.html");

        // /swagger-ui.html → /swagger-ui/index.html 리다이렉션
        registry.addRedirectViewController("/swagger-ui.html", "/swagger-ui/index.html");
    }
}
