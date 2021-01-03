import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/table/list',
    method: 'get',
    params
  })
}

export function getResourceList() {
  return request({
    url: '/upload/getResourceList',
    method: 'get',
    params: {}
  })
}

export function deleteResource(id) {
  return request({
    url: '/upload/deleteResource',
    method: 'get',
    params: { id }
  })
}

export function downloadFile(fileName) {
  return request({
    url: '/upload/download',
    method: 'get',
    params: {
      fileName
    }
  })
}
