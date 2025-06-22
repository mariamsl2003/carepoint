package com.capstone.demo.Config;

import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Member;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF protection if not needed
                .authorizeHttpRequests(auth -> {
                    // Allow access to these endpoints without authentication
                    auth.requestMatchers("/","/home", "/about", "/medical", "/medicine",
                            "/medical/medicals", "/medical/medicals/search", "/medicine/medicines",
                            "/medicine/medicines/search", "/search",  "/cards", "/users/signup", "/users/signing").permitAll()
                                    .requestMatchers("/profile", "/profile/edit", "/memberimg", "/volunteering",
                                            "/{id}/volunteer", "/donation" , "/new-donation", "/myrequest", "/mymedical",
                                            "mymedicine").authenticated()
                                    .requestMatchers("/pharmacist/validation", "/pharmacist/validate-search").authenticated()
                                    .requestMatchers("/admin/dashboard", "/admin/medi", "/admin/volunteer",
                                            "/admin/{id}/accept", "/admin/{id}/reject").hasAuthority("ADMIN");

                    auth.requestMatchers("/login").permitAll();
                    // Allow all other requests without authentication
                    auth.anyRequest().permitAll();
                })
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .permitAll()
                        .successHandler((request, response, authentication) -> {
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                            if (auth != null && auth.getPrincipal() != null) {
                                logger.info("Authenticated user: " + auth.getName());
                                logger.info("User roles: " + auth.getAuthorities());

                                String username = auth.getName();
                                Optional<MemberModel> member = memberRepository.findUserByUserName(username); // Inject userRepository
                                member.ifPresent(user -> request.getSession().setAttribute("loggedInUser", user));
                            }

                            if (authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN"))) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                               response.sendRedirect("/home");
                            }
                        })


                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
          .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
                .build();


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserInfoDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
}

}