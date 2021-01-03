import request from '@/utils/request'

export function addVideo(Eduvideo) {
  return request({
    url: '/couse/addVideo',
    method: 'post',
    data: Eduvideo
  })
}
