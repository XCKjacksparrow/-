/**
 * Copyright (C), 2015-2019
 * FileName: Base
 * Author:   A
 * Date:     2019/5/28 11:14
 * Description: 实体类的公共部分
 */
package cn.bestrivenlf.electronadmin.entity;


import cn.bestrivenlf.electronadmin.util.RandomNumberUtil;

import java.io.Serializable;

/**
 * 〈实体类的公共部分〉
 * @author dinggc
 * @create 2019/5/28
 * @since 1.0.0
 */
public class Base implements Serializable {
    /**
     * 主键id
     **/
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * @Author dinggc
     * @Description //设置实体类的id和时间
     * @Date 11:18 2019/5/28
     * @Param []
     * @return
     **/
    public Base() {
        this.id = RandomNumberUtil.getUUID();
    }
}
