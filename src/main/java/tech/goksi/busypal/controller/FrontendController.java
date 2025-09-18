package tech.goksi.busypal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/logs")
  public String logs() {
    return "logs";
  }

  @GetMapping("/login")
  public String login() {
    return "auth/login";
  }
}
