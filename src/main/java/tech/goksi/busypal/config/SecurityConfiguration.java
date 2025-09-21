package tech.goksi.busypal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.goksi.busypal.BusyPalEndpoint;
import tech.goksi.busypal.security.WhatsAppAuthenticationProvider;
import tech.goksi.busypal.security.configurer.WhatsAppAuthenticationConfigurer;
import tech.goksi.busypal.security.filter.LoginPageRedirectFilter;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity,
      LoginPageRedirectFilter loginPageRedirectFilter
  ) throws Exception {
    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(BusyPalEndpoint.LOGIN, "/error", "/css/**", "/img/**", "/js/**",
              "/ws/**")
          .permitAll();
      auth.anyRequest().authenticated();
    }).sessionManagement(session -> {
      session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }).with(new WhatsAppAuthenticationConfigurer<>(), waConfigurer -> {
      waConfigurer
          .loginPage(BusyPalEndpoint.LOGIN)
          .successForwardUrl(BusyPalEndpoint.INDEX)
          .loginProcessingUrl(BusyPalEndpoint.LOGIN);
    }).addFilterAfter(loginPageRedirectFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }


  @Bean
  public AuthenticationManager authenticationManager(
      HttpSecurity httpSecurity,
      WhatsAppAuthenticationProvider provider
  ) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(
        AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(provider);
    return authenticationManagerBuilder.build();
  }

}
