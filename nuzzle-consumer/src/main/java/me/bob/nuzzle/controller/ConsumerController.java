package me.bob.nuzzle.controller;

import me.bob.nuzzle.api.UserService;
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
