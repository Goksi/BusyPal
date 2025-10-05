package tech.goksi.busypal.manager;

public interface BusyManager {

  default void setBusy(String sessionId) {
    setBusy(sessionId, true);
  }

  default void setNotBusy(String sessionId) {
    setBusy(sessionId, false);
  }

  boolean isBusy(String sessionId);

  void setBusy(String sessionId, boolean busy);
}
