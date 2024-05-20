package com.security.jwttoken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.jwttoken.model.Role;
import com.security.jwttoken.services.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

    security
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
            .requestMatchers(
                "/auth/addNewUser/**",
                "/auth/generateToken/**",
                "/auth/all_user/**",
                "/auth/welcome",
                "/auth/delete/{id}/**")
            .permitAll()
            .requestMatchers("/auth/user/**").hasRole(Role.ROLE_USER.getValue())
            .requestMatchers("/auth/admin/**").hasRole(Role.ROLE_SADMIN.getValue())
            .requestMatchers("/private/user").hasRole(Role.ROLE_USER.getValue())
            .requestMatchers("/private/admin").hasAnyRole(Role.ROLE_ADMIN.getValue(), Role.ROLE_SADMIN.getValue())
            .requestMatchers("/private/sadmin").hasRole(Role.ROLE_SADMIN.getValue())
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/private/**")
            .hasAnyRole(
                Role.ROLE_ADMIN.getValue(),
                Role.ROLE_SADMIN.getValue())
            .anyRequest().authenticated())
        .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return security.build();

  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

}
