package vn.hust.easypos.config;

import javax.validation.Validator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class CustomConfiguration {

    @Bean
    public Validator customValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ModelMapper modelMapper() {
        // Create object ModelMapper and config mapping
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
