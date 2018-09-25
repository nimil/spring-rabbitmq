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
}
