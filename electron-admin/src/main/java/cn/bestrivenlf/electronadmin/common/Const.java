package cn.bestrivenlf.electronadmin.common;

/**
 * @Author: liufan
 * @Date: 2019/4/18 12:42
 * @Description:
 */
public class Const {
    //---------------成功代码--------------------//
    public static final  int SUCCESS_CODE = 200;

    //---------------系统认证错误--------------------//
    /** 未认证错误代码 **/
    public static final  int UNAUTHENTICATED_CODE = 1000;
    /** 未授权错误代码 **/
    public static final  int UNAUTHORIZED_CODE = 1001;
    //---------------登陆错误--------------------//
    /** 验证码错误代码 **/
    public static final  int WRONGCODE_CODE = 2000;
    /** 发送验证码错误代码 **/
    public static final  int SENDERROR_CODE = 2001;
    //---------------常规请求错误--------------------//
    /** 常规错误代码 **/
    public static final int NORMALERROR_CODE = 3000;

    //---------------Redis 的key-------------------//

    //---------------库存不足错误-------------------//
    public static final int ERROR_INVENTORY = 4000;

    //---------------消息队列常量------------------//
//    /** 延迟队列名 **/
//    public static final String AMQP_DELAY_QUEUE = "delayQueue";
//    /** 延迟交换机名 **/
//    public static final String AMQP_DELAY_EXCHANGE = "delayExchange";
//    /** 延迟路由 **/
//    public static final String AMQP_DELAY_ROUTEKEY = "delayKey";
//    /** 消费延迟队列 **/
//    public static final String AMQP_DELAY_CONSUMER = "delayConsumer";
//    /** 进入延迟队列交换机 **/
//    public static final String AMQP_DELAY_SENDEXCHANGE = "delaySendExchange";
//    /** 进入延迟队列路由 **/
//    public static final String AMQP_DELAY_SENDROUTEKEY = "delaySendRouteKey";
}
