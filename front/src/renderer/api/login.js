import request from '@/utils/request'

export function login(username, password, userip) {
  console.log(username + '  ' + password + '  userip  ' + userip)
  return request({
    url: '/user/userLogin',
    method: 'post',
    data: {
      username,
      password,
      userip
    }
  })
}

export function getInfo(userName) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { userName }
  })
}

// export function logout() {
//     return request({
//         url: '/user/logout',
//         method: 'post'
//     })
// }
export function logout(userip) {
  return request({
    url: '/user/logout',
    method: 'post',
    data: userip
  })
}
