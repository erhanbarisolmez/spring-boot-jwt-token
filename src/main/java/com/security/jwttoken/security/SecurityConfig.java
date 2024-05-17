package com.security.jwttoken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.jwttoken.config.PasswordEncoderConfig;
import com.security.jwttoken.model.Role;
import com.security.jwttoken.services.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  
  private final JwtAuthFilter jwtAuthFilter;
  private final UserService userService;
  private final PasswordEncoderConfig passwordEncoderConfig;

  public SecurityConfig(
      UserService userService,
      JwtAuthFilter jwtAuthFilter,
      PasswordEncoderConfig passwordEncoderConfig) {

    this.userService = userService;
    this.jwtAuthFilter = jwtAuthFilter;
    this.passwordEncoderConfig = passwordEncoderConfig;

  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

    security
        .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .csrf(
            csrfConfig -> csrfConfig.ignoringRequestMatchers("/public/**"))
        .userDetailsService(userService)
        .authorizeHttpRequests(x -> x
            .requestMatchers("/auth/addNewUser/**", "/auth/generateToken/**").permitAll()
            .requestMatchers("/auth/user/**").hasRole("USER")
            .requestMatchers("/auth/admin/**").hasRole("ADMIN")
            .requestMatchers("/private/user").hasRole(Role.ROLE_USER.getValue())
            .requestMatchers("/private/admin").hasAnyRole(Role.ROLE_ADMIN.getValue(), Role.ROLE_SADMIN.getValue())
            .requestMatchers("/private/sadmin").hasRole(Role.ROLE_SADMIN.getValue())
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/private/**")
            .hasAnyRole(
                Role.ROLE_ADMIN.getValue(),
                Role.ROLE_SADMIN.getValue())
            .anyRequest().authenticated())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


    return security.build();

  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userService);
    authenticationProvider.setPasswordEncoder(passwordEncoderConfig.bCryptPasswordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
    return configuration.getAuthenticationManager();
  }

}
