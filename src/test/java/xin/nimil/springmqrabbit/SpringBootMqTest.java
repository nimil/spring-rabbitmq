package xin.nimil.springmqrabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xin.nimil.springmqrabbit.pojo.Order;
import xin.nimil.springmqrabbit.producer.RabbitSender;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/10/21
 * @Time:10:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMqTest {

    @Test
    public void contextLoads(){


    }

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void testSender1() throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("number","12345");
        map.put("Time",new Date());
        rabbitSender.send("hello spring boot rabbit mqhahaha",map);
    }

    @Test
    public void testSender2() throws Exception{
        Order order = new Order();
        order.setId("2222");
        order.setName("testorder");
        order.setContent("test");
        rabbitSender.sendOrder(order);//发送消息的时候实体类一定要实现serilizebale接口才可以被消费者使用
    }


}
