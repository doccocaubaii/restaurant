package vn.hust.easypos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.hust.easypos.security.AuthoritiesConstants;
import vn.hust.easypos.security.jwt.JWTConfigurer;
import vn.hust.easypos.security.jwt.JWTFilter;
import vn.hust.easypos.security.jwt.JwtAuthenticationEntryPoint;
import vn.hust.easypos.security.jwt.TokenProvider;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfiguration {

    private final JWTFilter jwtFilter;

    private final TokenProvider tokenProvider;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfiguration(
        JWTFilter jwtFilter, TokenProvider tokenProvider,
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.jwtFilter = jwtFilter;
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(e -> e
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
//                    .anyRequest().permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

//                .requestMatchers("/app/*/*.{js,html}").permitAll()
                .requestMatchers(antMatcher("/app/*/*.{js,html}")).permitAll()
                .requestMatchers("/i18n/**").permitAll()
                .requestMatchers("/content/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/test/**").permitAll()
                .requestMatchers("/api/authenticate").permitAll()
                .requestMatchers("/api/register").permitAll()
                .requestMatchers("/api/activate").permitAll()
                .requestMatchers("/api/client/**").permitAll()
                .requestMatchers("/api/page/**").permitAll()
                .requestMatchers("/api/p/client/**").permitAll()
                .requestMatchers("/api/account/reset-password/init").permitAll()
                .requestMatchers("/api/account/reset-password/finish").permitAll()
                .requestMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/management/health").permitAll()
                .requestMatchers("/management/health/**").permitAll()
                .requestMatchers("/management/info").permitAll()
                .requestMatchers("/management/prometheus").permitAll()
                .requestMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
        return http.build();
        // @formatter:on
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
