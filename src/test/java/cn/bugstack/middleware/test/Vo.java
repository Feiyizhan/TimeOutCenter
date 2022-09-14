package cn.bugstack.middleware.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 测试Vo
 * @author 徐明龙 XuMingLong 2022-02-16
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class Vo implements Serializable {
    Integer id;
    String value;
}
