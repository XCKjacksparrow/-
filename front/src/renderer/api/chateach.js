import request from '@/utils/request'

export function getChatEachList(ip1,ip2) {
  return request({
    url: `/chateach/getAllChat/${ip1}/${ip2}`,
    method: 'get'
  })
}

export function addChatEach(userName1, ip1, userName2, ip2, content,fromIdentity1,fromIdentity2) {
  return request({
    url: '/chateach/addOneChat',
    method: 'post',
    data: {
      userName1,
      ip1,
      userName2,
      ip2,
      content: content,
      fromIdentity1,
      fromIdentity2
      
    }
  })
}
