package tech.goksi.busypal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tech.goksi.busypal.BusyPalEndpoint;

@Controller
public class FrontendController {

  @GetMapping(BusyPalEndpoint.INDEX)
  public String index() {
    return "index";
  }

  @GetMapping(BusyPalEndpoint.LOGS)
  public String logs() {
    return "logs";
  }

  @GetMapping(BusyPalEndpoint.LOGIN)
  public String login() {
    return "auth/login";
  }

  @PostMapping(BusyPalEndpoint.INDEX)
  public String indexRedirect() {
    return "redirect:/";
  }
}
