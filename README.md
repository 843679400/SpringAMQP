# SpringAMQP
针对springAMQP的学习记

学习参考网址
https://spring.io/projects/spring-amqp

maven依赖
<dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit</artifactId>
            <version>2.3.2</version>
</dependency>

1.配置RabbmitMQ配置类 
  配置rabbmitMQ连接工厂
  配置rabbitTemplate
  配置SimpleRabbitListenerContainerFactory 批量监听容器
  配置BatchingRabbitTemplate 批量发送(这个没有试出来)
2.配置消息消费模式
     SpringAMQP 提供了各种类型的交换机类型
     DirectExchange  直接消息模式  
     FanoutExchange  广播模式
     HeadersExchange  头模式 不是很了解这个模式
     TopicExchange   模糊匹配模式
3.消息异步监听
  采用@RabbitListener(queues = "topic2")注解的方式异步监听消息
