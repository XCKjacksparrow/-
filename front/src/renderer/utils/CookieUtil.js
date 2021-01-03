const session = require('electron').remote.session

export default class CookieUtils {
    static cookie={// 传入的数据类型
      url: '',
      name: '', // 一般传入ip
      value: '',
      expirationDate: Math.round(new Date().getTime() / 1000 * 60 * 60 * 24 * 7)// 默认保存七天
    }
    /**
     * 根据传入的ip获取用户类型（student/teacher）
     * @param {*} ip
     */
    static getCookie(ip) {
      session.defaultSession.cookies.get({ url: this.cookie.url }, (err, cookies) => {
        console.log(err)
        cookies.forEach((cookie, index) => {
          if (cookie.name === ip) {
            this.cookie = cookie
          }
        })
      })
    }
    /**
     * 设置cookie
     * @param {*} url
     * @param {*} name
     * @param {*} type
     */
    static setCookie(url, name, type) {
      this.cookie.url = url
      this.cookie.name = name
      this.cookie.value = type
      session.defaultSession.cookies.set(
        this.cookie
        , (err) => {
          console.log('Cookie存储失败')
          console.log(err)
        })
      console.log('setCookie方法结束')
    }
}
