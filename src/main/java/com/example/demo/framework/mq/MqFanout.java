package com.example.demo.framework.mq;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqFanout {
    /** 交换机名称 */
    public static String Exchange_Name = "fanoutExchange";
    /** 队列名称  */
    public static String Queue_Name = "fanoutQueue";
    /** 批量队列名称  */
    public static String Queue_Batch_Name = "fanoutQueueBatch";

    /**
     * 声明队列
     * @return
     */
    @Bean
    Queue fanoutQueue() {
        return new Queue(Queue_Name);
    }
    @Bean
    Queue fanoutBatchQueue() {
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
    FanoutExchange bindFanoutExchange(){
        /**
         * 构造一个新的Exchange，并为其指定名称，持久性标志和自动删除标志以及*参数。
         * @param name 交换的名称。
         * @param durable 如果我们声明一个持久交换（交换将在服务器重启后保留），则为true
         * @param autoDelete 如果服务器在不再使用该交换时应删除该交换，则为true
         * @param arguments 用于声明交换的参数
         */
        return new FanoutExchange(Exchange_Name,false,true,null);
    }

    /**
     * 将交换机与队列绑定
     * @return
     */
    @Bean
    Binding fanoutBindingOne(){
        return BindingBuilder.bind(fanoutQueue()).to(bindFanoutExchange());
    }
    @Bean
    Binding fanoutBindingBatch(){
        return BindingBuilder.bind(fanoutBatchQueue()).to(bindFanoutExchange());
    }
}
