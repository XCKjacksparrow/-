/**
 * 封装的MyWebSocket类
 * author:Liufan
 */
export default class MyWebSocket {
    /*
     *@description:消息体的基础构造
     *@author: LiuFan
     *@date: 2019-09-25 10:41:01
     */
    baseData = {
      fromIp: 'null', // 发送消息IP
      fromIdentity: 'null', // 发送消息的身份
      toIdentity: 'null', // 收获消息的身份
      toIp: 'null', // 收获消息的Ip
      username: 'null', // 用户名字
      type: 'null',
      data: {}
    }
  /*
         *@functionName:constructor
         *@params1: ip
         *@params2: port
         *@params3: localIp
         *@params4: identity
         *@description:
         *@author: LiuFan
         *@date: 2019-09-25 10:38:12
         */
    constructor(ip, port, localIp, data) {
      this.ip = ip
      this.localIp = localIp
      this.port = port
      this.identity = data.role
      // this.username = data.userName
      /**
                                     * 死数据

                                    if (data.userName === 'admin') {
                                      this.username = '王强'
                                    } else if (data.userName === '10000') {
                                      this.username = '李伟'
                                    } else {
                                      this.username = '张丽'
                                    }*/
      console.log('this.username')
      this.username = data.userName
      this.baseData = {
        fromIp: localIp, // 发送消息IP
        fromIdentity: this.identity, // 发送消息的身份
        toIdentity: 'null', // 收获消息的身份
        toIp: 'null', // 收获消息的Ip
        // username: this.username,
        type: 'null',
        data: {}
      }
      this.socket = this.createWebsocket()
    }
  /**
         * 建立webSocket连接方法
         * webSocket连接中弱化用户概念，单纯使用ip地址和身份标识进行区分，至于到底是哪个学生，则交给其他代码考虑，
         * websocket只考虑作为webRtc的信令服务器
         * 2019/9/24 author：liufan
         */
    createWebsocket() {
      const webSocket = new WebSocket(`ws://${this.ip}:${this.port}/${this.localIp}/${this.identity}`)
      webSocket.onclose = this.onClose
      webSocket.onerror = this.onError
      webSocket.onopen = this.onOpen
      webSocket.onmessage = this.onMessage
      return webSocket
    }

    /*
     *@functionName: sendMessage
     *@params1: data 消息的data
     *@params2: type 消息的类型
     *@description:发送消息，会将data封装进baseData的data属性里
     *@author: LiuFan
     *@date: 2019-09-25 10:48:51
     */
    sendMessage(data, type) {
      const d = this.baseData
      d.type = type
      d.data = data
      this.socket.send(JSON.stringify(d))
    }
  /*
         *@functionName:sendMessageToOne
         *@params1: data 消息的data
         *@params2: type 消息的类型
         *@params3: toIp 接收消息者的ip
         *@params4: identity 接收消息者的身份
         *@description: 发送消息给指定的某个人
         *@author: LiuFan
         *@date: 2019-09-25 12:53:36
         */
    sendMessageToAll(data, type) {
      this.baseData.toIp = 'null'
      this.sendMessage(data, type)
    }
  /*
         *@functionName:sendMessageToOne
         *@params1: data 消息的data
         *@params2: type 消息的类型
         *@params3: toIp 接收消息者的ip
         *@params4: identity 接收消息者的身份
         *@description: 发送消息给指定的某个人
         *@author: LiuFan
         *@date: 2019-09-25 12:53:36
         */
    sendMessageToOne(data, type, toIp, identity) {
      this.baseData.toIp = toIp
      this.baseData.toIdentity = identity

      this.sendMessage(data, type)
    }
  /*
         *@functionName: addMessageType
         *@params1{String}: type 消息类型
         *@params2{function}: callBack 解析方法
         *@description:添加自定义的消息解析方法
         *@author: LiuFan
         *@date: 2019-09-25 13:00:07
         */
    addMessageType(type, callBack) {
      messageType[type] = callBack
    }
  /*
         *@description: webSocket打开时候的回调函数
         *@author: LiuFan
         *@date: 2019-09-24 18:15:45
         */
    onOpen() {
      console.log('-----webSocket成功-----')
    }

    /*
     *@description: webScoket关闭时候的回调函数
     *@author: LiuFan
     *@date: 2019-09-24 18:17:15
     */
    onClose() {
      console.log('-----WebSocket关闭------')

      this.$store.dispatch('LogOut', this.localIp).then(() => {
        console.log('已经删除ip')
      })
    }

    /*
     *@functionName: onError
     *@description: websocket连接错误的回调函数
     *@author: LiuFan
     *@date: 2019-09-25 10:07:49
     */
    onError() {
      console.log('-----WebSocket错误------')
    }

    /*
     *@functionName: onMessage
     *@params1: message 收到的消息，String，一般为JSON格式
     *@description: websocket收到消息的处理方法
     *@author: LiuFan
     *@date: 2019-09-25 10:07:49
     */
    onMessage(message) {
      const body = JSON.parse(message.data)
      const type = body.type
      console.log('收到消息:' + type)
      // 此方法中的this指向WebScoket的实例对象，而不是本对象
      if (messageType.hasOwnProperty(type)) {
        // 有这个消息类型的处理方法
        messageType[type](body)
      } else {
        // 没有这个消息类型的处理方法
        console.log('没有这个消息类型的处理方法:' + type)
      }
    }

    setOnOpen(func) {
      this.socket.onopen = func
    }

    setOnError(func) {
      this.socket.onerror = func
    }

    setOnClose(func) {
      console.log('websocket关闭了')
      this.socket.onclose = func
    }
}

/*
 *@description:消息类型的处理函数配置
 *@author: LiuFan
 *@date: 2019-09-25 10:27:47
 */
const messageType = {
  'msg': (body) => {
    console.log('this is msg process in webSocket.js')
    console.log(body)
  }
}
