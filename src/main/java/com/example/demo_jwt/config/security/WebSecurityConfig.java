package com.example.demo_jwt.config.security;

import com.example.demo_jwt.config.jwt.JwtEntryPoint;
import com.example.demo_jwt.config.jwt.JwtTokenFilter;
import com.example.demo_jwt.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration

public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                  .authorizeRequests().requestMatchers("/login").permitAll()
                  .requestMatchers("/api/auth/all").permitAll()
                  .requestMatchers("/api/auth/users").hasAnyAuthority("ADMIN","USER")
                  .requestMatchers("/api/auth/admin").hasAuthority("ADMIN")


                .requestMatchers(HttpMethod.GET,"/api/users").hasAnyAuthority("ADMIN","USER")
                .requestMatchers(HttpMethod.POST,"/api/signup").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/remove/{id}").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/404")
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter(encoder().encode("password"))
                .and()
                .httpBasic()

        ;
        http.exceptionHandling().authenticationEntryPoint(jwtEntryPoint);
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
