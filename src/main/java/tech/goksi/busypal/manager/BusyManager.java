package tech.goksi.busypal.manager;

public interface BusyManager {

  boolean isBusy(String sessionId);

  void setBusy(String sessionId, boolean busy);
}
