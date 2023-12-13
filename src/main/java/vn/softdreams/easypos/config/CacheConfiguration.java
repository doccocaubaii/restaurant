package vn.softdreams.easypos.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Bean
    public Cache cacheOne() {
        return new GuavaCache("1", CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build());
    }
}
