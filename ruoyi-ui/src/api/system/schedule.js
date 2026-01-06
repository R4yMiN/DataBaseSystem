import request from '@/utils/request'

// 查询VIEW列表
export function listSchedule(query) {
  return request({
    url: '/system/schedule/list',
    method: 'get',
    params: query
  })
}

// 查询VIEW详细
export function getSchedule(id) {
  return request({
    url: '/system/schedule/' + id,
    method: 'get'
  })
}

// 新增VIEW
export function addSchedule(data) {
  return request({
    url: '/system/schedule',
    method: 'post',
    data: data
  })
}

// 修改VIEW
export function updateSchedule(data) {
  return request({
    url: '/system/schedule',
    method: 'put',
    data: data
  })
}

// 删除VIEW
export function delSchedule(id) {
  return request({
    url: '/system/schedule/' + id,
    method: 'delete'
  })
}
