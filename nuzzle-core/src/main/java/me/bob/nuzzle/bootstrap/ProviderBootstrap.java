package me.bob.nuzzle.bootstrap;

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

    @PostConstruct
    public void start() {
        buildProvider();
        initRpcServer();
    }

    private void buildProvider() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            EnableProvider provider = AnnotationUtils.findAnnotation(bean.getClass(), EnableProvider.class);
            if (provider == null) {
                continue;
            }
            Class<?>[] classes = bean.getClass().getInterfaces();
            if (classes == null || classes.length == 0) {
                continue;
            }
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

    }
}