package xin.nimil.springmqrabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import xin.nimil.springmqrabbit.pojo.Order;

import java.util.Map;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/10/21
 * @Time:10:25
 */
@Component
public class RabbitSender {

    @Autowired
private RabbitTemplate rabbitTemplate;

    final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {

            System.out.println("correlationData");
            System.out.println(correlationData);
            System.out.println(b);
            if (!b){
                System.out.println("异常处理");
            }

        }
    } ;

    final ReturnCallback returnCallback = new ReturnCallback(){
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int i, String s, String s1, String s2) {
            System.out.println("return exchange:"+s1+"routing key"+s2+s);
        }
    };

    public void send(Object message, Map<String,Object> properties) throws Exception{
        MessageHeaders mhs = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message,mhs);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("123123123"); //这个就是实际消息的id,全局唯一的消息id 可以通过这个id取到一条唯一的消息
        //如果消息找不到路由键会走return方法，会返回相应的参数效果
        rabbitTemplate.convertAndSend("exchange-1","springboot.hello",msg,correlationData);
    }

    public void sendOrder(Order order) throws Exception{
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("1232222123123"); //这个就是实际消息的id,全局唯一的消息id 可以通过这个id取到一条唯一的消息
        //如果消息找不到路由键会走return方法，会返回相应的参数效果
        rabbitTemplate.convertAndSend("exchange-2","springboot.def",order,correlationData);
    }





}
