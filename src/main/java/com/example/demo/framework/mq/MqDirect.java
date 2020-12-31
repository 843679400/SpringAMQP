package com.example.demo.framework.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1v1模式
 */
@Configuration
public class MqDirect {
    /** 交换机名称 */
    public static String Exchange_Name = "directExchange";
    /** 队列名称  */
    public static String Queue_Name = "myqueue";
    /** 批量队列名称  */
    public static String Queue_Batch_Name = "myqueueBatch";

    /** 路由键 */
    public static String Rout_key = "myqueue";
    public static String Rout_key_Batch = "myqueueBatch";

    /**
     * 声明队列
     * @return
     */
    @Bean
    Queue myQueue() {
        return new Queue(Queue_Name);
    }
    @Bean
    Queue batchQueue() {
        return new Queue(Queue_Batch_Name);
    }

    /**
     * SpringAMQP 提供了各种类型的交换机类型
     * DirectExchange  1v1队列  正在使用
     * FanoutExchange  广播模式
     * HeadersExchange  头模式 不是很了解这个模式
     * TopicExchange   模糊匹配模式
     * @return
     */
    @Bean
    DirectExchange bindExchangeOne(){
        /**
         * 构造一个新的Exchange，并为其指定名称，持久性标志和自动删除标志以及*参数。
         * @param name 交换的名称。
         * @param durable 如果我们声明一个持久交换（交换将在服务器重启后保留），则为true
         * @param autoDelete 如果服务器在不再使用该交换时应删除该交换，则为true
         * @param arguments 用于声明交换的参数
         */
        return new DirectExchange(Exchange_Name,false,true,null);
    }

    /**
     * 将交换机与队列绑定同时设置路由键
     * @return
     */
    @Bean
    Binding directBindingOne(){
        return BindingBuilder.bind(myQueue()).to(bindExchangeOne()).with(Rout_key);
    }
    @Bean
    Binding directBindingBatch(){
        return BindingBuilder.bind(batchQueue()).to(bindExchangeOne()).with(Rout_key_Batch);
    }
}
