package vn.softdreams.easypos.config;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configure the converters to use the ISO format for dates by default.
 */
@Configuration
public class FileConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = SystemUtils.OS_NAME;
        if (os.contains("Windows")) {
            registry.addResourceHandler("/client/file/**").addResourceLocations("file:///C:/service/shared_file/");
        } else {
            registry.addResourceHandler("/client/file/**").addResourceLocations("file:/DATA_EASYPOS/");
        }
    }
}
