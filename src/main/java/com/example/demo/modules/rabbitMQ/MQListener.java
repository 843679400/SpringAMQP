package com.example.demo.modules.rabbitMQ;


import com.example.demo.framework.mq.MqDirect;
import com.sun.istack.internal.logging.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MQListener {

    private Logger log = Logger.getLogger(MQListener.class);


    /**
     * 消息一条一条打印
     * @param msg
     */
    @RabbitListener(queues = "myqueue")
    public void direct(String msg){
        log.info("myqueue监听到的消息--->"+msg);
    }

    /**
     * 消息批量接收
     * @param messages
     */
    @RabbitListener(queues = "myqueueBatch",containerFactory = "batchQueueRabbitListenerContainerFactory")
    public void directBatch(List<Message> messages){
        log.info("myqueueBatch监听到的消息数量--->"+messages.size());
    }

    /**
     * 中国
     * @param msg
     */
    @RabbitListener(queues = "topic1")
    public void topic1(String msg){
        log.info("topic1监听到的消息--中国--->"+msg);
    }

    /**
     * 男
     * @param msg
     */
    @RabbitListener(queues = "topic2")
    public void topic2(String msg){
        log.info("topic2监听到的消息--男--->"+msg);
    }

    /**
     * 唱歌
     * @param msg
     */
    @RabbitListener(queues = "topic3")
    public void topic3(String msg){
        log.info("topic3监听到的消息--唱歌--->"+msg);
    }

    /**
     * 消息一条一条打印
     * @param msg
     */
    @RabbitListener(queues = "fanoutQueue")
    public void fanout(String msg){
        log.info("fanoutQueue监听到的消息----->"+msg);
    }

    @RabbitListener(queues = "fanoutQueueBatch",containerFactory = "batchQueueRabbitListenerContainerFactory")
    public void fanoutBatch(List<Message> messages){
        log.info("fanoutQueueBatch监听到的数量----->"+messages.size());
        log.info("fanoutQueueBatch监听到的消息----->"+messages.get(0).toString());
    }
}
