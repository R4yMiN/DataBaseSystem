import request from '@/utils/request'

// 查询学生自主选课列表
export function listSelection(query) {
  return request({
    url: '/system/selection/list',
    method: 'get',
    params: query
  })
}

// 查询学生自主选课详细
export function getSelection(id) {
  return request({
    url: '/system/selection/' + id,
    method: 'get'
  })
}

// 新增学生自主选课
export function addSelection(data) {
  return request({
    url: '/system/selection',
    method: 'post',
    data: data
  })
}

// 修改学生自主选课
export function updateSelection(data) {
  return request({
    url: '/system/selection',
    method: 'put',
    data: data
  })
}

// 删除学生自主选课
export function delSelection(id) {
  return request({
    url: '/system/selection/' + id,
    method: 'delete'
  })
}

// 选择课程（学生操作）
export function selectSelection(id) {
  return request({
    url: '/system/selection/select/' + id,
    method: 'post'
  })
}

// 退选课程（学生操作）
export function unselectSelection(id) {
  return request({
    url: '/system/selection/unselect/' + id,
    method: 'post'
  })
}
