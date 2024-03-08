package com.hust.interviewmanagement.config;

import com.hust.interviewmanagement.controllerAdvice.CustomAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SessionFilterCustom sessionFilterCustom;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        a -> a
                                .requestMatchers("/admin/offer/**", "/offer").hasAnyRole("MANAGER", "RECRUITER")
                                .requestMatchers("/admin/**", "/admin").hasAnyRole("ADMIN", "RECRUITER", "INTERVIEW", "MANAGER")
                                .anyRequest().permitAll())
                .oauth2Login(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/oauth",true)
                        .permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/signIn")
                        .successForwardUrl("/")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .exceptionHandling(err -> err.accessDeniedPage("/403"))
                .addFilterAfter(sessionFilterCustom, BasicAuthenticationFilter.class);
//        config tam thời
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
