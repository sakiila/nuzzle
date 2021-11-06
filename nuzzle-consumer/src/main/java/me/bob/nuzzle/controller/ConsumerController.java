package me.bob.nuzzle.controller;

import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.api.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@RestController
@Slf4j
public class ConsumerController {

    private UserService userService;

    @RequestMapping("/hello")
    public String sayHi(String name) {
        try (Socket socket = new Socket("127.0.0.1", 8090)) {

            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            final String request = "Hello!";
            objectOutputStream.writeObject(request);

            final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            final String messaage = (String) objectInputStream.readObject();
            return messaage;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Exception", e);
        }
        return "";
    }

}
