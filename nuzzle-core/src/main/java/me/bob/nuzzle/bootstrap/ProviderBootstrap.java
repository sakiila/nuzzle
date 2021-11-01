package me.bob.nuzzle.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import me.bob.nuzzle.annotation.EnableProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@ConditionalOnBean(name = "nuzzleProviderApplication")
public class ProviderBootstrap {

    @Autowired
    private ApplicationContext applicationContext;

    EventLoopGroup bossEventLoop = new NioEventLoopGroup();

    EventLoopGroup workerEventLoop = new NioEventLoopGroup();

    @PostConstruct
    public void start() {
        buildProvider();
        initRpcServer();
    }

    /**
     * 构建生产者
     */
    private void buildProvider() {
        // 从应用上下文获取 bean
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            // 拿到以 EnableProvider 注解的类
            EnableProvider provider = AnnotationUtils.findAnnotation(bean.getClass(), EnableProvider.class);
            if (provider == null) {
                continue;
            }
            Class<?>[] classes = bean.getClass().getInterfaces();
            if (classes == null || classes.length == 0) {
                continue;
            }
            // 创建生产者
            Arrays.stream(classes).forEach(c -> createProvider(c, bean));
        }
    }

    /**
     * 创建生产者
     *
     * @param clazz
     * @param bean
     */
    private void createProvider(Class<?> clazz, Object bean) {

    }

    /**
     * 初始化服务端
     */
    private void initRpcServer() {
        // 使用 Netty 构造服务端
        ServerBootstrap nettyBoot = new ServerBootstrap();

        final LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4, true);

        nettyBoot.group(bossEventLoop, workerEventLoop)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(lengthFieldPrepender);
                    }
                });

        try {
            int listenPort = 6300;
            nettyBoot.bind(listenPort).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
