import request from '@/utils/request'

export function getChatList() {
  return request({
    url: '/chat/getAllChat',
    method: 'get'
  })
}

export function addChat(userName, ip, content, fromIdentity) {
  return request({
    url: '/chat/addOneChat',
    method: 'post',
    data: {
      userName,
      ip,
      content: content,
      fromIdentity
    }
  })
}
