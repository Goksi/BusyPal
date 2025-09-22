package tech.goksi.busypal.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseWsEvent {

  @JsonProperty("event_type")
  private final String eventType;

  public BaseWsEvent(String eventType) {
    this.eventType = eventType;
  }

  public String getEventType() {
    return eventType;
  }
}
