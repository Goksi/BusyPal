package tech.goksi.busypal;

public class BusyPalProperties {

    private Device device;
    private long loginTimeout;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public long getLoginTimeout() {
        return loginTimeout;
    }

    public void setLoginTimeout(long loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    public static class Device {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
