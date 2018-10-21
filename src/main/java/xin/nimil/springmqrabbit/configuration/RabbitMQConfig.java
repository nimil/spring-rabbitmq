package xin.nimil.springmqrabbit.configuration;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xin.nimil.springmqrabbit.consumer.Myconsumer;
import xin.nimil.springmqrabbit.consumer.TextMessageConvert;
import xin.nimil.springmqrabbit.converter.MessageDelegate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/9/19
 * @Time:22:19
 */
//@Configuration
//@ComponentScan("xin.nimil.*")
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("192.168.199.101");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);
    }

    @Bean
    public Binding bingding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);
    }

    @Bean
    public Binding bingding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true);
    }

    @Bean
    public Binding bingding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }


    @Bean
    public Queue queue_image() {
        return new Queue("image_queue", true);
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("pdf_queue", true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue001(), queue002(), queue003(), queue_image(), queue_pdf());
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
        simpleMessageListenerContainer.setDefaultRequeueRejected(false);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        simpleMessageListenerContainer.setExposeListenerChannel(true);

        simpleMessageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return s + UUID.randomUUID().toString();
            }
        });

        /**
         simpleMessageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
        @Override public void onMessage(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        System.out.println(msg);
        }
        });
         **/
        //委派对象 支持消息的转换
        /**
         MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new Myconsumer());
         messageListenerAdapter.setDefaultListenerMethod("consumerMsg");
         //也可以使用字符串的方式
         messageListenerAdapter.setMessageConverter(new TextMessageConvert());

         simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
         **/
        //第二种模式  队列名称和方法名称进行一一匹配
        /** MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new Myconsumer());


         Map<String, String> stringStringMap = new HashMap<>();

         stringStringMap.put("queue001","method1");
         stringStringMap.put("queue002","method2");
         messageListenerAdapter.setQueueOrTagToMethodName(stringStringMap);
         //也可以使用字符串的方式
         messageListenerAdapter.setMessageConverter(new TextMessageConvert());

         simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
         **/

        /**
         MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new Myconsumer());
         messageListenerAdapter.setDefaultListenerMethod("consumerMessage");
         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
         messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);
         simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
         **/

         MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new Myconsumer());
         messageListenerAdapter.setDefaultListenerMethod("consumerMessage");
         Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

         //这个地方一定不能用默认的转换器否则会报错得覆盖原来的转换器  设置信任包才可以
         DefaultJackson2JavaTypeMapper jackson2JavaTypeMapper = new RabbitMqFastJsonClassMapper();
         jackson2JsonMessageConverter.setJavaTypeMapper(jackson2JavaTypeMapper);

         messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);
         simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);


        /**
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new Myconsumer());
        messageListenerAdapter.setDefaultListenerMethod("consumerMessage");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

        //这个地方一定不能用默认的转换器否则会报错得覆盖原来的转换器  设置信任包才可以
        DefaultJackson2JavaTypeMapper jackson2JavaTypeMapper = new RabbitMqFastJsonClassMapper();

        Map<String, Class<?>> idClassMapping = new HashMap<>();

        //多对象定义转换
        idClassMapping.put("order", xin.nimil.springmqrabbit.pojo.Order.class);
        idClassMapping.put("packaged", xin.nimil.springmqrabbit.pojo.Packaged.class);

        jackson2JavaTypeMapper.setIdClassMapping(idClassMapping);
        jackson2JsonMessageConverter.setJavaTypeMapper(jackson2JavaTypeMapper);
        messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);

        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
**/
        return simpleMessageListenerContainer;
    }

}
