package me.bob.nuzzle.stub;

import java.lang.reflect.Method;

public class StubSkeletonHelper {

    public static void createProvider(Class<?> clazz, Object serviceImpl) {
        String className = clazz.getName();
        Class<?> callClass = serviceImpl.getClass();
        Method[] methods = callClass.getMethods();
        for (Method method : methods) {

        }
    }
}
