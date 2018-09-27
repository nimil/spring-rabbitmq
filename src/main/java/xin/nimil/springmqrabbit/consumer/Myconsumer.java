package xin.nimil.springmqrabbit.consumer;



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

}
