package me.bob.nuzzle.proxy;

import io.netty.bootstrap.Bootstrap;
import org.springframework.cglib.proxy.Enhancer;

public class NuzzleProxyFactory {

    public static Object getProxy(Class<?> clazz, Bootstrap bootstrap) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(clazz.getClassLoader());
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new NuzzleInterceptor(clazz, bootstrap));
        return enhancer.create();
    }
}
