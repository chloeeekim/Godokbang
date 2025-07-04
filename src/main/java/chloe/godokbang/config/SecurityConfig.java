package chloe.godokbang.config;

import chloe.godokbang.auth.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/", "/login", "/join", "/login-error", "/css/**").permitAll()
                       .anyRequest().permitAll() // 테스트용 - 추후 authenticated로 바꿀 것
               )
               .formLogin(form -> form
                       .loginPage("/login")
                       .loginProcessingUrl("/login")
                       .usernameParameter("email")
                       .defaultSuccessUrl("/home", true)
                       .failureForwardUrl("/login-error")
                       .permitAll()
               )
               .logout(logout -> logout
                       .logoutUrl("/logout")
                       .logoutSuccessUrl("/")
                       .invalidateHttpSession(true)
                       .deleteCookies("JSESSIONID")
                       .permitAll()
               )
               .userDetailsService(userDetailService);
       return http.build();
    }
}
