<template>
  <div class="login-container">
    <el-form
      class="login-form"
      autoComplete="on"
      :model="loginForm"
      :rules="loginRules"
      ref="loginForm"
      label-position="left"
    >
      <transition>
        <h3 class="title reveal">
          <span style="animation-delay: 0.9s;">虚</span>
          <span style="animation-delay: 0.8s;">拟</span></span>
          <span style="animation-delay: 0.7s;">仿</span>
          <span style="animation-delay: 0.6s;">真</span>
          <span style="animation-delay: 0.5s;">实</span>
          <span style="animation-delay: 0.4s;">验</span>
          <span style="animation-delay: 0.3s;">课</span>
          <span style="animation-delay: 0.4s;">堂</span>
          <span style="animation-delay: 0.5s;">管</span>
          <span style="animation-delay: 0.6s;">理</span>
          <span style="animation-delay: 0.7s;">系</span>
          <span style="animation-delay: 0.8s;">统</span>
        </h3>
      </transition>
      
      <el-form-item prop="username">
        <span class="svg-container svg-container_login">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          name="username"
          type="text"
          v-model="loginForm.username"
          autoComplete="on"
          placeholder="username"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"></svg-icon>
        </span>
        <el-input
          name="password"
          :type="pwdType"
          @keyup.enter.native="handleLogin"
          v-model="loginForm.password"
          autoComplete="on"
          placeholder="password"
        ></el-input>
        <span class="show-pwd" @click="showPwd"
          ><svg-icon icon-class="eye"
        /></span>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          style="width: 100%"
          :loading="loading"
          @click.native.prevent="handleLogin"
        >
          登&nbsp;录
        </el-button>
      </el-form-item>
      <div class="tips">
        <!-- <span style="margin-right:20px;">username: admin</span>
        <span> password: admin</span> -->
      </div>
    </el-form>
  </div>
</template>
<script>
import { isvalidUsername } from '@/utils/validate'
import myWebRtc from '@/utils/webRTC'
import CookieUtils from '@/utils/CookieUtil'
import MyWebScoket from '@/utils/webSocket'
import Vue from 'vue'
export default {
  name: 'login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error('请输入正确的用户名'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('密码不能小于5位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: 'admin',
        password: 'admin'
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePass }
        ]
      },
      loading: false,
      pwdType: 'password'
    }
  },
  // mounted: function(){
  //                         let duration = 0.8;
  //         let delay = 0.3;
  //         let revealText = document.querySelector(".reveal");
  //         console.log(revealText);
  //         let letters = revealText.textContent.split("");
  //         revealText.textContent = "";
  //         let middle = letters.filter(e => e !== " ").length / 2;
  //         console.log("执行了1")
  //         letters.forEach((letter, i) => {
  //           let span = document.createElement("span");
  //           console.log("执行了2")
  //           span.textContent = letter;
  //           span.style.animationDelay = `${delay + Math.abs(i - middle) * 0.1}s`;
  //           revealText.append(span);
  //           console.log("执行了3")
  //         });
  // },
  methods: {
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const formdata = { ...this.loginForm, userip: this.localIp }
          this.$store
            .dispatch('Login', formdata)
            .then((data) => {
              this.loading = false
              // 登陆成功
              // 进行webSocket连接服务器的操作
              // TODO:该部分属于webSocket连接，应当放在登陆成功后，这样才可以获取到身份信息
              // const ip = require('ip')
              // const localIp = ip.address()
              const socket = new MyWebScoket(
                this.serverIp,
                this.serverPort,
                this.localIp,
                data
              )
              Vue.prototype.socket = socket
              CookieUtils.setCookie(
                'http://' + this.serverIp + ':8080',
                this.localIp,
                data.role
              ) // 存到cookie当中，在VUEX之前加载
              this.$store.dispatch('set_socket', socket)
              this.$router.push({ path: '/' })
            })
            .catch(() => {
              this.loading = false
            })
          // this.loading = false
          // this.$router.push({ path: '/' })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
$bg: #2d3a4b;
$light_gray: #eee;

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
.login-container {
  display: flex;
  min-height: 100vh;
  // background-color: $bg;
  background-image: url("./bgzip.jpg") !important;
  background-size: 100% 100%;
  background-repeat: no-repeat;

  .login-form {
    margin: auto;
    min-width: 313px;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    &_login {
      font-size: 20px;
    }
  }
  .title {
    font-size: 26px;
    font-weight: 400;
    color: $light_gray;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
.reveal {
  position: relative;
  display: flex;
  color: #6ee1f5;
  font-size: 2em;
  font-family: Raleway, sans-serif;
  letter-spacing: 3px;
  text-transform: uppercase;
  white-space: pre;

  span {
    opacity: 0;
    transform: scale(0);
    animation: fadeIn 2.4s forwards;
  }

  &::before,
  &::after {
    position: absolute;
    content: "";
    top: 0;
    bottom: 0;
    width: 2px;
    height: 100%;
    background: white;
    opacity: 0;
    transform: scale(0);
  }

  &::before {
    left: 50%;
    animation: slideLeft 1.5s cubic-bezier(0.7, -0.6, 0.3, 1.5) forwards;
  }

  &::after {
    right: 50%;
    animation: slideRight 1.5s cubic-bezier(0.7, -0.6, 0.3, 1.5) forwards;
  }
}

@keyframes fadeIn {
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes slideLeft {
  to {
    left: -6%;
    opacity: 1;
    transform: scale(0.9);
  }
}

@keyframes slideRight {
  to {
    right: -6%;
    opacity: 1;
    transform: scale(0.9);
  }
}
</style>
