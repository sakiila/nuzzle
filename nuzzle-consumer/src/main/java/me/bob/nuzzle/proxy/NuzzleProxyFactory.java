package me.bob.nuzzle.proxy;

import io.netty.bootstrap.Bootstrap;
import org.springframework.cglib.proxy.Enhancer;

public class NuzzleProxyFactory {

    public static Object getProxy(Class<?> clazz, Bootstrap bootstrap) {
        // 创建动态代理增强类
        final Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(clazz);
        // 设置方法拦截器
        enhancer.setCallback(new NuzzleInterceptor(clazz, bootstrap));
        // 创建代理类
        return enhancer.create();
    }
}
