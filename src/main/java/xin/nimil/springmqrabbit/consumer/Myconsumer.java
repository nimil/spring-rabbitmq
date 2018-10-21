package xin.nimil.springmqrabbit.consumer;


import xin.nimil.springmqrabbit.pojo.Order;
import xin.nimil.springmqrabbit.pojo.Packaged;

import java.util.Map;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/9/25
 * @Time:22:16
 */
public class Myconsumer {

//    public void consumerMsg(byte[] msgBody){
//        System.out.println(new String(msgBody));
//    }

    public void consumerMsg(String msg){
        System.out.println(msg);
    }

    public void method1(String messageBody){
        System.out.println("Myconsumer.method1");
        System.out.println(messageBody);
    }

    public void method2(String messageBody){
        System.out.println("Myconsumer.method2");
        System.out.println(messageBody);
    }

    public void consumerMessage(Map messageBody){
        System.out.println("map方法：消息内容："+messageBody);
    }

    public void consumerMessage(Order order){
        System.out.println("order对象：消息内容："+order.getContent());
    }
    public void consumerMessage(Packaged packaged){
        System.out.println("packaged对象：消息内容："+packaged.getDescription());
    }
}
