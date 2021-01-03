import request from '@/utils/request'

export function addChapter(eduChapter) {
  return request({
    url: '/couse/savechapter',
    method: 'post',
    data: eduChapter
  })
}

export function GetChapterVideo(courseId) {
  return request({
    url: `/couse/getChapterVideoById/${courseId}`,
    method: 'get'

  })
}
