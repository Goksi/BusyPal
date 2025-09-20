package tech.goksi.busypal.event.publisher.impl;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tech.goksi.busypal.event.BaseWsEvent;
import tech.goksi.busypal.event.publisher.EventPublisher;

public abstract class AbstractSimpEventPublisher implements EventPublisher {

  private final SimpMessagingTemplate template;

  protected AbstractSimpEventPublisher(SimpMessagingTemplate template) {
    this.template = template;
  }

  @Override
  public void publishEvent(String session, String destination, BaseWsEvent event) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
        .create(SimpMessageType.MESSAGE);
    headerAccessor.setSessionId(session);
    headerAccessor.setLeaveMutable(true);
    template.convertAndSendToUser(
        session,
        destination,
        event,
        headerAccessor.getMessageHeaders()
    );
  }
}
