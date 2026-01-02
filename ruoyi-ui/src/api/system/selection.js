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

// 选课
export function selectSelection(id) {
  return request({
    url: '/system/selection/select/' + id,
    method: 'put'
  })
}

// 退选
export function unselectSelection(id) {
  return request({
    url: '/system/selection/unselect/' + id,
    method: 'put'
  })
}

// 删除学生自主选课
export function delSelection(id) {
  return request({
    url: '/system/selection/' + id,
    method: 'delete'
  })
}

// 获取当前学生的已选课程列表（课表）
export function getMySchedule() {
  return request({
    url: '/system/selection/mySchedule',
    method: 'get'
  })
}
