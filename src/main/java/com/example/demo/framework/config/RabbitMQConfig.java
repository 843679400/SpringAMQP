package com.example.demo.framework.config;

import com.example.demo.framework.mq.MqDirect;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * rabbmitMQ配置
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 配置基础客户端连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        /** admin是我自己创建的账户 */
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }



    /**
     * 源码中会发现rabbitTemplate实现自amqpTemplate接口，使用起来并无区别
     * 提供消息发布接收基本方法
     * @return
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    /**
     * 配置批量监听容器
     * @param connectionFactory
     * @return
     */
    @Bean("batchQueueRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory batchQueueRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置批量
        factory.setBatchListener(true);
        //设置BatchMessageListener生效
        factory.setConsumerBatchEnabled(true);
        //设置监听器一次批量处理的消息数量
        factory.setBatchSize(10);
        //设置线程数
        factory.setConcurrentConsumers(10);
        //最大线程数
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }

    @Bean("batchQueueTaskScheduler")
    public TaskScheduler batchQueueTaskScheduler(){
        TaskScheduler taskScheduler=new ThreadPoolTaskScheduler();
        return taskScheduler;
    }

    /**
     * 配置批量发送
     * @return
     */
    @Bean("batchingRabbitTemplate")
    public BatchingRabbitTemplate batchingRabbitTemplate(ConnectionFactory connectionFactory,
                                                         @Qualifier("batchQueueTaskScheduler") TaskScheduler taskScheduler){
        //!!!重点： 所谓批量， 就是spring 将多条message重新组成一条message, 发送到mq, 从mq接受到这条message后，在重新解析成多条message
        //一次批量的数量
        int batchSize=10;
        // 缓存大小限制,单位字节，
        // simpleBatchingStrategy的策略，是判断message数量是否超过batchSize限制或者message的大小是否超过缓存限制，
        // 缓存限制，主要用于限制"组装后的一条消息的大小"
        // 如果主要通过数量来做批量("打包"成一条消息), 缓存设置大点
        // 详细逻辑请看simpleBatchingStrategy#addToBatch()
        int bufferLimit=1024; //1 K
        long timeout=10000;
        //注意，该策略只支持一个exchange/routingKey
        //A simple batching strategy that supports only one exchange/routingKey
        BatchingStrategy batchingStrategy=new SimpleBatchingStrategy(batchSize,bufferLimit,timeout);
        return new BatchingRabbitTemplate(connectionFactory,batchingStrategy,taskScheduler);
    }
}
