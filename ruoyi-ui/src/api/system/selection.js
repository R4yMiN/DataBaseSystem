import request from '@/utils/request'

// 查询选课大厅列表
export function listSelection(query) {
  return request({
    url: '/school/selection/list',
    method: 'get',
    params: query
  })
}

// 我的已选课程（顶部小表）
export function getMySchedule() {
  return request({
    url: '/school/selection/mySchedule',
    method: 'get'
  })
}

// 选课操作
export function selectSelection(classId) {
  return request({
    url: '/school/selection',
    method: 'post',
    data: { classId: classId }
  })
}

// 退选操作
export function unselectSelection(selectionId) {
  return request({
    url: '/school/selection/' + selectionId,
    method: 'delete'
  })
}

// 删除学生自主选课
export function delSelection(id) {
  return request({
    url: '/school/selection/' + id,
    method: 'delete'
  })
}

