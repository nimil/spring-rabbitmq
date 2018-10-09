package xin.nimil.springmqrabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xin.nimil.springmqrabbit.pojo.Order;
import xin.nimil.springmqrabbit.pojo.Packaged;

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
    public void testAdmin() throws Exception {
        rabbitAdmin.declareExchange(new DirectExchange("test_spring_direct", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test_spring_topic", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test_spring_faunt", false, false));
        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareBinding(new Binding("test.direct.queue", Binding.DestinationType.QUEUE, "test_spring_direct", "test", null));
    }

    @Test
    public void testSendMsg() throws Exception {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述");
        messageProperties.getHeaders().put("type", "自定义消息类型");
        Message message = new Message("hello rabbit".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("添加额外的设置。。。。");
                message.getMessageProperties().getHeaders().put("desc", "额外信息修改");
                message.getMessageProperties().getHeaders().put("addr", "testst");
                return message;
            }
        });

    }

    @Test
    public void testSendMsg2() throws Exception {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("hello rabbitssss".getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.rabbit", message);
        rabbitTemplate.send("topic002", "rabbit.abc", message);
        // rabbitTemplate.convertAndSend("topic002","rabbit.abc","hello ran");
    }


    @Test
    public void testSendJsonMsg() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单信息");
        order.setContent("描述信息");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println(json);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");

        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);

    }

    @Test
    public void testSendJavaMsg() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单信息");
        order.setContent("描述信息");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println("order json format"+json);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__","xin.nimil.springmqrabbit.pojo.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);

    }

    @Test
    public void testSendMappingMsg() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单信息");
        order.setContent("描述信息");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println("order json format"+json);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__","order");
        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "spring.order", message);

        Packaged packaged = new Packaged();
        packaged.setId("001");
        packaged.setDescription("描述信息222");
        packaged.setName("这是pack");

        String json1 = objectMapper.writeValueAsString(packaged);
        System.out.println("packaged json format"+json1);

        MessageProperties messageProperties2 = new MessageProperties();
        messageProperties2.setContentType("application/json");
        messageProperties2.getHeaders().put("__TypeId__","packaged");
        Message message2 = new Message(json1.getBytes(), messageProperties2);
        rabbitTemplate.send("topic001", "spring.order", message2);

    }

}
