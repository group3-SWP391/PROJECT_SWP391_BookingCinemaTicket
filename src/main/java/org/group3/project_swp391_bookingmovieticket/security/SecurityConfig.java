package org.group3.project_swp391_bookingmovieticket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())  // Cho phép mọi request
                .csrf(csrf -> csrf.disable())                                 // Tắt CSRF
                .httpBasic(httpBasic -> httpBasic.disable())                  // Tắt HTTP Basic Auth
                .formLogin(formLogin -> formLogin.disable())                  // Tắt form login mặc định
                .logout(logout -> logout.disable());                          // Tắt logout

        return http.build();
    }
}
