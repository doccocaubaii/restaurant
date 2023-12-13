package vn.softdreams.easypos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final AccountExpiredInterceptor accountExpiredInterceptor;

    public InterceptorConfig(AccountExpiredInterceptor accountExpiredInterceptor) {
        this.accountExpiredInterceptor = accountExpiredInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountExpiredInterceptor);
    }
}
