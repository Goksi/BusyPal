package tech.goksi.busypal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @RequestMapping("/logs")
  public String logs() {
    return "logs";
  }

  @RequestMapping("/login")
  public String login() {
    return "auth/login";
  }
}
