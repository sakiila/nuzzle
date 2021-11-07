package me.bob.nuzzle.serializer;

import java.io.ObjectInputStream;

public interface Serializer {

    byte[] serialize(Object object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
