package me.bob.nuzzle.impl;

import me.bob.nuzzle.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public String sayHi(String name) {
        return "Hi, this is UserServiceImpl";
    }
}
