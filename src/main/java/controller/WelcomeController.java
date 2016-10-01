package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Michał on 2016-10-01.
 */

@Controller
@EnableAutoConfiguration
public class WelcomeController {

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "HELLO HEROKU!";
    }

    public static void main(String[] args) {
        SpringApplication.run(WelcomeController.class, args);
    }
}
