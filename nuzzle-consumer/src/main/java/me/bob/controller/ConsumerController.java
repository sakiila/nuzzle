package me.bob.controller;

import me.bob.api.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private UserService userService;

    @RequestMapping("/hello")
    public String sayHi(String name) {
        return "";
    }

}
