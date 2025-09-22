package tech.goksi.busypal.security.model;

import java.io.Serializable;

public record WhatsAppPrincipal(String phoneNumber) implements Serializable {

}
