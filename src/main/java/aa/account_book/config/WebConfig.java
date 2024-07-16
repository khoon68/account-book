package aa.account_book.config;

import aa.account_book.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.rmi.registry.Registry;

@Configuration
public class WebConfig {

    public void addInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/detail/**")
                .excludePathPatterns("/", "/register", "/login");
    }
}
