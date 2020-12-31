package com.example.demo.framework.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模糊匹配
 * 定义路由键匹配规则
 * a.b.c
 * a表示性别  boy男,gril女
 * b表示国籍  中国china , 美国us , 英国ua
 * c表示爱好  sing唱歌 dancing跳舞
 * 实例 爱唱歌的中国男 boy.china.sing
 *     中国人         *.china.*
 *     男人          boy.*.* || boy.#
 *     *（星号）可以代替一个单词。
 *     #（哈希）可以替代零个或多个单词。
 */
@Configuration
public class MqTopic {
    /** 交换机名称 */
    public static String Exchange_Name = "topicExchange";
    /** 队列名称  */
    public static String Queue_Name1 = "topic1";
    /** 队列名称  */
    public static String Queue_Name2 = "topic2";
    /** 队列名称  */
    public static String Queue_Name3 = "topic3";
    /** 批量队列名称  */
    public static String Queue_Batch_Name = "topicQueueBatch";

    /** 路由键 */
    public static String Rout_key1 = "*.china.*";
    public static String Rout_key2 = "boy.#";
    public static String Rout_key3 = "#.sing";
    public static String Rout_key_Batch = "topic.batch";

    /**
     * 声明队列
     * @return
     */
    @Bean
    Queue myQueue1() {
        return new Queue(Queue_Name1);
    }
    @Bean
    Queue myQueue2() {
        return new Queue(Queue_Name2);
    }
    @Bean
    Queue myQueue3() {
        return new Queue(Queue_Name3);
    }
    @Bean
    Queue batchQueueTopic() {
        return new Queue(Queue_Batch_Name);
    }

    /**
     * SpringAMQP 提供了各种类型的交换机类型
     * DirectExchange  1v1队列
     * FanoutExchange  广播模式
     * HeadersExchange  头模式 不是很了解这个模式
     * TopicExchange   模糊匹配模式 正在使用
     * @return
     */
    @Bean
    TopicExchange bindTopicExchange(){
        /**
         * 构造一个新的Exchange，并为其指定名称，持久性标志和自动删除标志以及*参数。
         * @param name 交换的名称。
         * @param durable 如果我们声明一个持久交换（交换将在服务器重启后保留），则为true
         * @param autoDelete 如果服务器在不再使用该交换时应删除该交换，则为true
         * @param arguments 用于声明交换的参数
         */
        return new TopicExchange(Exchange_Name,false,true,null);
    }

    /**
     * 将交换机与队列绑定同时设置路由键
     * @return
     */
    @Bean
    Binding topicBindingOne(){
        return BindingBuilder.bind(myQueue3()).to(bindTopicExchange()).with(Rout_key3);
    }
    @Bean
    Binding topicBindingtwo(){
        return BindingBuilder.bind(myQueue2()).to(bindTopicExchange()).with(Rout_key2);
    }
    @Bean
    Binding topicBindingthere(){
        return BindingBuilder.bind(myQueue1()).to(bindTopicExchange()).with(Rout_key1);

    }
    @Bean
    Binding topicBindingBatch(){
        return BindingBuilder.bind(batchQueueTopic()).to(bindTopicExchange()).with(Rout_key_Batch);
    }
}
