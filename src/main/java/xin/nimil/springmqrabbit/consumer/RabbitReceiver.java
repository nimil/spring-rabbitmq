package xin.nimil.springmqrabbit.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/10/21
 * @Time:15:02
 */
@Component
public class RabbitReceiver {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",durable = "${spring.rabbitmq.listener.order.exchange.durable}",type = "${spring.rabbitmq.listener.order.exchange.type}",ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key="${spring.rabbitmq.listener.order.exchange.key}"
    ))
    @RabbitHandler
    public void onMsg(Message message, Channel channel) throws Exception{
        System.out.println(message.getPayload());//消费端收到消息体内容
        Long de = (long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(de,false);//不使用ack将无法消费消息，消息在服务端阻塞
    }

    @RabbitHandler
    public void orderMsg(@Payload xin.nimil.springmqrabbit.pojo.Order order, Channel channel, @Headers Map<String,Object> headers) throws Exception{
        System.out.println(order);
        Long deliverytag = (long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliverytag,false);

    }

}
