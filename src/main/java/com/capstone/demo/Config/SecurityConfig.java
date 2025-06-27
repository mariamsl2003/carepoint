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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
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
                    auth.requestMatchers("/","/home", "/about", "/search",
                                    "/users/signup", "/users/signing", "/users/login", "/users/pass").permitAll()
                                    .requestMatchers("/profile", "/profile/edit", "/volunteering",
                                            "/volunteer", "/donation" , "/new-donation", "/myrequest", "/mymedical",
                                            "mymedicine", "/request").authenticated()
                                    .requestMatchers("/pharmacist/validation", "/pharmacist/validate-search").authenticated()
                                    .requestMatchers("/admin/dashboard", "/admin/medi", "/admin/volunteer",
                                            "/admin/{id}/accept", "/admin/{id}/reject").hasAuthority("ADMIN");
                    // Allow all other requests without authentication
                    auth.anyRequest().permitAll();
                })
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/logging")
                        .permitAll()
                        .successHandler((request, response, authentication) -> {
                            logger.info("User  authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                            logger.info("get to know the email");

                            if (auth == null) {
                                logger.warn("❌ Authentication object is NULL (User is not logged in)");
                            } else if (auth.getPrincipal() == null) {
                                logger.warn("⚠️ Authentication exists, but Principal is NULL (Possible session issue)");
                            }

                            if (auth != null && auth.getPrincipal() != null) {
                                String email = auth.getName();
                                logger.info("looking for user with email: {} ", auth.getName());
                                Optional<MemberModel> member = memberRepository.findMemberByEmail(email); // Inject userRepository
                                logger.info("looked for the user with email: {}", email);
                                if(member.isPresent()){
                                    MemberModel user = member.get();
                                    request.getSession().setAttribute("loggedInUser", user);
                                    logger.info("Authenticated user: " +user.getEmail());
                                    logger.info("going to see the role of user: {}", user.getRole());
                                    if (authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN"))) {
                                        logger.info("here it is admin: {}", user.getRole());
                                        response.sendRedirect("/admin/dashboard");
                                    } else {
                                        logger.info("here should be member or pharmacist: {}", user.getRole());
                                        response.sendRedirect("/home");
                                    }
                                }
                                else{
                                    logger.info("user {} not found", email);
                                }

                            }


                        })


                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
          .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                  .maximumSessions(1)
                  .expiredUrl("/users/login?expired")
                  .sessionRegistry(sessionRegistry())
        );

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
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}