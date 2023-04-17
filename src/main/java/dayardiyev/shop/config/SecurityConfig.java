package dayardiyev.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests((request) -> request
                .requestMatchers("/registration", "/login").permitAll()
                .requestMatchers("/admin/**").hasAnyRole("admin", "moder")
                .anyRequest().authenticated()
        );

        http.formLogin((form) -> {
            form.loginPage("/login")
                    .loginProcessingUrl("/login_process")
                    .defaultSuccessUrl("/products", true);
        });

        http.logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
