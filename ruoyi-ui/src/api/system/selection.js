import request from '@/utils/request'

// 选课大厅：获取所有老师开的课
export function listSelection(query) {
  return request({
    url: '/school/selection/list',
    method: 'get',
    params: query
  })
}

// 我的课表：获取当前学生选中的课
export function getMySchedule() {
  return request({
    url: '/school/selection/mySchedule',
    method: 'get'
  })
}

// 选课
export function addSelection(data) {
  return request({
    url: '/school/selection',
    method: 'post',
    data: data
  })
}

// 退选
export function delSelection(id) {
  return request({
    url: '/school/selection/' + id,
    method: 'delete'
  })
}
