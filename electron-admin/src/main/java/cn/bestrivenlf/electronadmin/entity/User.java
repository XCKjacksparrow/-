package cn.bestrivenlf.electronadmin.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: liufan
 * @Date: 2019/10/2 11:12
 * @Description:
 */
@Data
@Accessors(chain = true)
public class User {
    private int id;
    private String userName;
    private String password;
    private String role;
    private String ip;
}
