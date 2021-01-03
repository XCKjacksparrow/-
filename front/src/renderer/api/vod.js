
import request from '@/utils/request'

export function getPlayAuth(videoId) {
  return request({
    url: `/eduvod/video/getVideoPlayAuth/${videoId}`,
    method: 'get'
  })
}

