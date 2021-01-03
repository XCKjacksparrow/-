export default class Client {
    conn = null; // socket连接       用于与服务器发送json格式数据
    connectedUser = null; // 对等连接的用户     这里用学生与老师的ip来代替

    constructor(userIp) {
      this.conn = new WebSocket('ws://192.168.1.105:9090')// 与server.js创建socket连接
      // 下面是绑定方法
      this.conn.onerror = this.onerror
      this.conn.onopen = this.onopen
      this.conn.onmessage = this.onmessage
      this.conn.handleLogin = this.handleLogin
      this.conn.handleOffer = this.handleOffer
      this.conn.handleAnswer = this.handleAnswer
      this.conn.handleCandidate = this.handleCandidate
      this.conn.handleLeave = this.handleLeave

      this.conn.connectedUser = this.connectedUser
      this.conn.stream = null // 语音流
      this.conn.remoteStream = null // 对话的流
      this.conn.yourConn = null // 对等连接

      // 用户登录
      const name = userIp // 这里改成用户的ip
      if (name.length > 0) {
        setTimeout(() => {
          this.conn.send(JSON.stringify({
            type: 'login',
            name: name
          }))
        }, 300)
      }
    }

    onopen() {
      console.log('Connected to the signaling server')
    }

    // 此方法是socket的方法, 用于接受server.js的数据
    onmessage(msg) {
      var that = this
      console.log('Got message', msg.data)
      var data = JSON.parse(msg.data)
      switch (data.type) {
        case 'login':
          that.handleLogin(data.success)
          break
        case 'offer':
          that.handleOffer(data.offer, data.name)
          break
        case 'answer':
          that.handleAnswer(data.answer)
          break
          // when a remote peer sends an ice candidate to us
        case 'candidate':
          that.handleCandidate(data.candidate)
          break
        case 'leave':
          that.handleLeave()
          break
        default:
          break
      }
    }

    onerror(err) {
      console.log('Got error', err)
    }

    // 此方法是socket的方法, 用于与server.js发送数据
    send(message) {
      // attach the other peer username to our messages
      if (this.connectedUser) {
        message.name = this.connectedUser
      }
      this.conn.send(JSON.stringify(message))
    }

    // 用户登录, 这里是服务器返回登录信息
    handleLogin(success) {
      const that = this
      if (success === false) {
        alert('Ooops...try a different username')
      } else {
        // loginPage.style.display = "none";
        // callPage.style.display = "block";

        //* *********************
        // Starting a peer connection
        //* *********************

        // getting local audio stream
        navigator.webkitGetUserMedia({ video: false, audio: true }, function(myStream) {
          that.stream = myStream

          // displaying local audio stream on the page
          // try {
          //     localAudio.srcObject = stream;
          // } catch (error) {
          //     localAudio.src = window.URL.createObjectURL(stream);
          // }
          // localAudio.src = window.URL.createObjectURL(stream);

          // using Google public stun server
          var configuration = {
            'iceServers': [{ 'url': 'stun:stun2.1.google.com:19302' }]
          }

          that.yourConn = new webkitRTCPeerConnection(configuration)

          // setup stream listening
          that.yourConn.addStream(that.stream)

          // when a remote user adds stream to the peer connection, we display it
          that.yourConn.onaddstream = function(e) {
            console.log('new stream', e.stream)
            that.remoteStream = e.stream

            try {
              document.querySelector(
                '#remoteAudio'
              ).srcObject = e.stream
            } catch (error) {
              document.querySelector(
                '#remoteAudio'
              ).src = window.URL.createObjectURL(e.stream)
            }
            // try {
            //   remoteAudio.srcObject = that.stream
            // } catch (error) {
            //   remoteAudio.src = window.URL.createObjectURL(that.stream)
            // }
          }

          // Setup ice handling
          that.yourConn.onicecandidate = function(event) {
            if (event.candidate) {
              that.send(JSON.stringify({
                type: 'candidate',
                candidate: event.candidate,
                name: that.connectedUser
              }))
            }
          }
        }, function(error) {
          console.log(error)
        })
      }
    }

    // 开始对话  外部调用此方法来开始对话
    callUser(userIp) { // userIp指的是学生的ip
      var callToUsername = userIp

      if (callToUsername.length > 0) {
        this.connectedUser = callToUsername

        console.log(this)
        const that = this
        // create an offer
        this.conn.yourConn.createOffer(function(offer) {
          that.conn.send(JSON.stringify({
            type: 'offer',
            offer: offer,
            name: callToUsername
          }))

          that.conn.yourConn.setLocalDescription(offer)
        }, function(error) {
          alert('Error when creating an offer')
        })
      }
    }

    // when somebody sends us an offer
    handleOffer(offer, name) {
      this.connectedUser = name
      this.yourConn.setRemoteDescription(new RTCSessionDescription(offer))

      // create an answer to an offer
      const that = this
      this.yourConn.createAnswer(function(answer) {
        that.yourConn.setLocalDescription(answer)
        that.send(JSON.stringify({
          type: 'answer',
          answer: answer,
          name: name
        }))
      }, function(error) {
        alert('Error when creating an answer')
      })
    }

    // when we got an answer from a remote user
    handleAnswer(answer) {
      this.yourConn.setRemoteDescription(new RTCSessionDescription(answer))
    }

    // when we got an ice candidate from a remote user
    handleCandidate(candidate) {
      this.yourConn.addIceCandidate(new RTCIceCandidate(candidate))
    }

    // hang up   调用此方法挂断语音
    hangUp() {
      this.send(JSON.stringify({
        type: 'leave'
      }))
      handleLeave()
    }

    // 用户离开
    handleLeave() {
      this.connectedUser = null
      this.remoteAudio.src = null
      this.yourConn.close()
      this.yourConn.onicecandidate = null
      this.yourConn.onaddstream = null
    }
}
