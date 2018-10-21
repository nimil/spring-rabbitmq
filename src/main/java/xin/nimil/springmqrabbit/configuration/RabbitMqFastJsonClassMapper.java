package xin.nimil.springmqrabbit.configuration;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/10/9
 * @Time:22:14
 */
public class RabbitMqFastJsonClassMapper extends DefaultJackson2JavaTypeMapper {

    /**
     * 构造函数初始化信任所有pakcage
     */
    public RabbitMqFastJsonClassMapper() {
        super();
        setTrustedPackages("*");
    }

}
