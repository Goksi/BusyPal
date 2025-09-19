package tech.goksi.busypal;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class TestMemoryAppender extends ListAppender<ILoggingEvent> {

    public boolean contains(String log) {
        return this.list
                .stream()
                .anyMatch(logEvent -> logEvent.toString().contains(log));
    }

    public void reset() {
        this.list.clear();
    }
}
