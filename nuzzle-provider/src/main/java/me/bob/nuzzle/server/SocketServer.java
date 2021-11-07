package me.bob.nuzzle.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Service
@Slf4j
public class SocketServer {

//    @PostConstruct
    public void init() {
        try (ServerSocket server = new ServerSocket(8099)) {
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected");
                try (final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
                    final String messaage = (String) objectInputStream.readObject();
                    log.info("message is: {}", messaage);
                    
                    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    final String response = "Hola!";
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Exception", e);
                }
            }
        } catch (IOException e) {
            log.error("Exception", e);
        }
    }
}
