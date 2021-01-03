import request from '@/utils/request'

export function getlist() {
  return request({
    url: '/eduservice/index/index',
    method: 'get'

  })
}
