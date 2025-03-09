package org.lidiannan.demo.config;

import org.lidiannan.demo.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 方法 1: 使用 AntPathRequestMatcher
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/comments")).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginProcessingUrl("/api/auth/login")
                                .successHandler((request, response, authentication) -> {
                                    response.setStatus(200);
                                    response.getWriter().write("{\"success\":true}");
                                })
                                .failureHandler((request, response, exception) -> {
                                    response.setStatus(401);
                                    response.getWriter().write("{\"success\":false,\"message\":\"" + exception.getMessage() + "\"}");
                                })
                )
                .logout(logout -> logout
                                .logoutUrl("/api/auth/logout")
                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                );
            
        return http.build();
    }
}

// 方法 2: 如果使用的是 Spring Security 5.x，可以使用以下配置
/*
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/comments").permitAll()
            .anyRequest().authenticated()
        .and()
        .formLogin()
            .loginProcessingUrl("/api/auth/login")
            .successHandler((request, response, authentication) -> {
                response.setStatus(200);
                response.getWriter().write("{\"success\":true}");
            })
            .failureHandler((request, response, exception) -> {
                response.setStatus(401);
                response.getWriter().write("{\"success\":false,\"message\":\"" + exception.getMessage() + "\"}");
            })
        .and()
        .logout()
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");
        
    return http.build();
}
*/

// 方法 3: 如果使用的是 Spring Security 6.x，還可以嘗試這種方式
/*
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/api/comments").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginProcessingUrl("/api/auth/login")
            .successHandler((request, response, authentication) -> {
                response.setStatus(200);
                response.getWriter().write("{\"success\":true}");
            })
            .failureHandler((request, response, exception) -> {
                response.setStatus(401);
                response.getWriter().write("{\"success\":false,\"message\":\"" + exception.getMessage() + "\"}");
            })
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        );
        
    return http.build();
}
*/