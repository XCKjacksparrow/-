import request from '@/utils/request'

export function getPeoples() {
  return request({
    url: '/socket/getPeoples',
    method: 'get',
    data: {
    }
  })
}

