package tech.goksi.busypal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.goksi.busypal.BusyPalProperties;

@Configuration
public class PropertiesConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "busypal")
  public BusyPalProperties busyPalProperties() {
    return new BusyPalProperties();
  }
}
