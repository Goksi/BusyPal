package tech.goksi.busypal.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import tech.goksi.busypal.BusyPalProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

  private final BusyPalProperties properties;

  public WebSocketConfiguration(BusyPalProperties properties) {
    this.properties = properties;
  }
  
  @Override
  public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {
    registry.addEndpoint("ws")
        .setAllowedOrigins(properties.getAllowedWsOrigins())
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/busypal");
  }
}
