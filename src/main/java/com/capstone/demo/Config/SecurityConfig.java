package com.capstone.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF protection if not needed
                .authorizeHttpRequests(auth -> {
                    // Allow access to these endpoints without authentication
                    auth.requestMatchers("/home", "/about", "/medicine", "/medicine/add", "/medicine/medicines",
                            "/medicine/medicineimg", "/medicine/medicines/search").permitAll();

                    auth.requestMatchers("/login").permitAll();
                    // Allow all other requests without authentication
                    auth.anyRequest().permitAll();
                })
                .formLogin(form -> form
                        .defaultSuccessUrl("/home", true) // Redirect users to home page after login
                        .permitAll() // Allow all users to access the login page
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));

        return http.build();
    }
}