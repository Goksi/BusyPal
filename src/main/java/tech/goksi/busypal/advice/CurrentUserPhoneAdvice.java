package tech.goksi.busypal.advice;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

@ControllerAdvice
public class CurrentUserPhoneAdvice {

  @ModelAttribute("currentPhone")
  public String phoneNumber(Authentication authentication) {
    if (authentication == null) {
      return null;
    }
    var principal = authentication.getPrincipal();
    if (principal instanceof WhatsAppPrincipal(String phoneNumber)) {
      return phoneNumber;
    }
    return null;
  }
}
