package xin.nimil.springmqrabbit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:nimil e-mail:nimilgg@qq.com
 * @Date:2018/10/8
 * @Time:21:54
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order implements Serializable {
    private String id;
    private String name;
    private String content;
}
