package tech.goksi.busypal.manager.impl;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import tech.goksi.busypal.manager.BusyManager;

@Service
public class SimpleInMemoryBusyManager implements BusyManager {

  private final Set<String> busySessions;

  public SimpleInMemoryBusyManager() {
    this.busySessions = new HashSet<>();
  }

  @Override
  public boolean isBusy(String sessionId) {
    return busySessions.contains(sessionId);
  }

  @Override
  public void setBusy(String sessionId, boolean busy) {
    if (busy) {
      busySessions.add(sessionId);
    } else {
      busySessions.remove(sessionId);
    }
  }
}
