package com.poseidon.poseidon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).authorizeRequests(auth -> {
                    auth.antMatchers("/").permitAll();
                    auth.antMatchers("/admin/**").hasRole("ADMIN");
                    auth.antMatchers("/user/**").hasRole("ADMIN");
                    auth.antMatchers("/user/add").permitAll();
                    auth.antMatchers(HttpMethod.POST, "/user/validate").anonymous();
                    auth.antMatchers("/css/bootstrap.min.css").permitAll();
                    auth.antMatchers("/image/github-mark.svg").permitAll();
                    auth.anyRequest().authenticated();
                }).formLogin(formLogin -> {
                    formLogin.loginPage("/login");
                    formLogin.defaultSuccessUrl("/bidList/list");
                    formLogin.permitAll();
                }).logout(logout -> {
                    logout.logoutUrl("/app-logout");
                    logout.deleteCookies("JSESSIONID");
                })
                .oauth2Login(oauth2Login -> {
                    oauth2Login.loginPage("/login");
                    // oauth2Login.loginProcessingUrl("/login"); // TODO créer utlisateur en base ??
                    oauth2Login.defaultSuccessUrl("/bidList/list");
                    oauth2Login.permitAll();
                })
                .build();
    }

}
