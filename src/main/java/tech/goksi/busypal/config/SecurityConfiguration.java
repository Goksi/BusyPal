package tech.goksi.busypal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import tech.goksi.busypal.BusyPalEndpoint;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(BusyPalEndpoint.LOGIN, "/error", "/css/**", "/img/**", "/js/**", "/ws/**")
          .permitAll();
      auth.anyRequest().authenticated();
    }).sessionManagement(session -> {
      session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }).exceptionHandling(exceptionHandling -> {
      exceptionHandling
          .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(BusyPalEndpoint.LOGIN));
    });

    return httpSecurity.build();
  }

}
