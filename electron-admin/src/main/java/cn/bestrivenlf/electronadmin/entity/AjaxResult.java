package cn.bestrivenlf.electronadmin.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: liufan
 * @Date: 2019/4/19 14:58
 * @Description:
 */
@Data
public class AjaxResult {
    private int code;
    private Object data;
    private String msg = "ok";
    private Date time = new Date();

    public AjaxResult(int code, Object data){
        this.code = code;
        this.data = data;
    }
    public AjaxResult(int code){
        if(code== 200){
            this.code = code;
        }else{
            this.code = code;
            this.msg = "error";
        }
    }
    public AjaxResult(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
