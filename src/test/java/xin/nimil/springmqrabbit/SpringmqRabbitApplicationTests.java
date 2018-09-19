package xin.nimil.springmqrabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringmqRabbitApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

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

}
