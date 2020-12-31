package com.example.demo;

import com.example.demo.framework.mq.MqDirect;
import com.example.demo.framework.mq.MqFanout;
import com.example.demo.framework.mq.MqTopic;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class RabbmitMqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private BatchingRabbitTemplate batchingRabbitTemplate;

    /**
     * 单条消息
     */
    @Test
    void direct(){
        rabbitTemplate.convertAndSend(MqDirect.Exchange_Name,MqDirect.Rout_key,"我是消息");
    }

    @Test
    void topic(){
        //中国人
        rabbitTemplate.convertAndSend
                (MqTopic.Exchange_Name,"boy.china.sing",getMsg("男","中国","唱歌"));
        rabbitTemplate.convertAndSend
                (MqTopic.Exchange_Name,"girl.china.dancing",getMsg("女","中国","跳舞"));
        //唱歌的人
        rabbitTemplate.convertAndSend
                (MqTopic.Exchange_Name,"girl.us.sing",getMsg("女","美国","唱歌"));
        //男人
        rabbitTemplate.convertAndSend
                (MqTopic.Exchange_Name,"boy.ua.dancing",getMsg("男","英国","跳舞"));
    }

    @Test
    void directBatch(){
        for(int i=0;i<100;i++){
            rabbitTemplate.convertAndSend(MqDirect.Exchange_Name,MqDirect.Rout_key_Batch,"我是消息");
        }
    }

    @Test
    void fanout(){
        rabbitTemplate.convertAndSend(MqFanout.Exchange_Name,null,"fanout消息");
    }

    @Test
    void delete() throws IOException {

        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        channel.queuePurge("xiaomi");
        channel.queueDelete("xiaomi");
        //channel.exchangeDelete("exchangeName");

    }

    @Test
    void sendBatch(){
        // 除了send(String exchange, String routingKey, Message message, CorrelationData correlationData)方法是发送单条数据
        // 其他send都是批量发送
        for(int i=0;i<10;i++){
            Message message = new Message((i+"你好啊").getBytes(),new MessageProperties());
            batchingRabbitTemplate.send(MqDirect.Exchange_Name,MqDirect.Rout_key,message);
        }

    }

    public String getMsg(String 性别,String 国籍,String 爱好){
        String msg = "性别:{a},国籍:{b},爱好:{c}";
        return msg.replace("{a}",性别)
                .replace("{b}",国籍)
                .replace("{c}",爱好);
    }
}
