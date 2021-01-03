import request from '@/utils/request'

export function getuserlist() {
  return request({
    url: '/user/getuserlist',
    method: 'get'
  })
}

export function getuserlistById(name) {
  return request({
    url: `/user/getuserlistById/${name}`,
    method: 'get'
  })
}

export function getonlineuserlist() {
  return request({
    url: '/user/getonlineuserlist',
    method: 'get'
  })
  
}
export function getonlineteacherlist() {
  return request({
    url: '/user/getonlineteacherlist',
    method: 'get'
    })
  }


export function getNameByIp(ip) {
  return request({
    url: `/user/getuserByIp/${ip}`,
    method: 'get'
    })
  }



