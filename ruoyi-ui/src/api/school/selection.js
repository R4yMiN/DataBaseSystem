import request from '@/utils/request'

// 查询选课成绩列表
export function listSelection(query) {
  return request({
    url: '/school/selection/list',
    method: 'get',
    params: query
  })
}

// 查询选课成绩详细
export function getSelection(selectionId) {
  return request({
    url: '/school/selection/' + selectionId,
    method: 'get'
  })
}

// 新增选课成绩
export function addSelection(data) {
  return request({
    url: '/school/selection',
    method: 'post',
    data: data
  })
}

// 修改选课成绩
export function updateSelection(data) {
  return request({
    url: '/school/selection',
    method: 'put',
    data: data
  })
}

// 删除选课成绩
export function delSelection(selectionId) {
  return request({
    url: '/school/selection/' + selectionId,
    method: 'delete'
  })
}
