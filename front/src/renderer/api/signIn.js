import request from '@/utils/request'

export function getResourceList() {
  return request({
    url: '/user/getSignList',
    method: 'get'
  })
}
