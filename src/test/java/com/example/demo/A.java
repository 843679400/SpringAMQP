package com.example.demo;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.io.IOException;

public class A {
    public static void main(String[] args) throws IOException {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        /** admin是我自己创建的账户 */
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        channel.queueDeclare("ccc123",true,true,true,null);
//        channel.queuePurge("xiaomi");
//        channel.queueDelete("xiaomi");
    }
}
