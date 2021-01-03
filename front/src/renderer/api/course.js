import request from '@/utils/request'

export function addCourse(course) {
  return request({
    url: '/couse/addCourseInfo',
    method: 'post',
    data: course
  })
}

export function getPublishCourseInfo(id) {
  return request({
    url: `/couse/getCoursePublishVoById/${id}`,
    method: 'get'
  })
}

export function PublishCourseInfo(id) {
  return request({
    url: `/couse/publishCourseInfo/${id}`,
    method: 'post'
  })
}

export function getCourseWeb(courseId) {
  return request({
    url: `/eduservice/index/getCourseWeb/${courseId}`,
    method: 'get'
  })
}

