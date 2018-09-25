package xin.nimil.springmqrabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringmqRabbitApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {


    }

    @Test
    public void testAdmin() throws Exception{
        rabbitAdmin.declareExchange(new DirectExchange("test_spring_direct",false,false));
        rabbitAdmin.declareExchange(new TopicExchange("test_spring_topic",false,false));
        rabbitAdmin.declareExchange(new FanoutExchange("test_spring_faunt",false,false));
        rabbitAdmin.declareQueue(new Queue("test.direct.queue",false));
        rabbitAdmin.declareBinding(new Binding("test.direct.queue", Binding.DestinationType.QUEUE,"test_spring_direct","test",null));
    }

    @Test
    public void testSendMsg() throws Exception{
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc","信息描述");
        messageProperties.getHeaders().put("type","自定义消息类型");
        Message message = new Message("hello rabbit".getBytes(),messageProperties);

        rabbitTemplate.convertAndSend("topic001","spring.amqp",message, new MessagePostProcessor(){
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("添加额外的设置。。。。");
                message.getMessageProperties().getHeaders().put("desc","额外信息修改");
                message.getMessageProperties().getHeaders().put("addr","testst");
                return message;
            }
        });

    }
    @Test
    public void testSendMsg2() throws Exception{
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("hello rabbitssss".getBytes(),messageProperties);

        rabbitTemplate.send("topic001","spring.rabbit",message);
        rabbitTemplate.convertAndSend("topic002","rabbit.abc","hello ran");
}



}
