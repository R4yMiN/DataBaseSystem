import request from '@/utils/request'

// 查询院系列表
export function listDepartment(query) {
  return request({
    url: '/school/department/list',
    method: 'get',
    params: query
  })
}

// 查询院系详细
export function getDepartment(deptId) {
  return request({
    url: '/school/department/' + deptId,
    method: 'get'
  })
}

// 新增院系
export function addDepartment(data) {
  return request({
    url: '/school/department',
    method: 'post',
    data: data
  })
}

// 修改院系
export function updateDepartment(data) {
  return request({
    url: '/school/department',
    method: 'put',
    data: data
  })
}

// 删除院系
export function delDepartment(deptId) {
  return request({
    url: '/school/department/' + deptId,
    method: 'delete'
  })
}
