package tech.goksi.busypal.manager.impl;

import org.springframework.stereotype.Service;
import tech.goksi.busypal.manager.ResponseManager;

@Service
public class SimpleStaticResponseManager implements ResponseManager {

  private static final String BUSY_MESSAGE = "Sorry, I'm currently busy and will replay when I can";

  @Override
  public String getResponse(String message) {
    return BUSY_MESSAGE;
  }
}
