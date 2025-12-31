import request from '@/utils/request'

// 查询开课列表
export function listClass(query) {
  return request({
    url: '/school/class/list',
    method: 'get',
    params: query
  })
}

// 查询开课详细
export function getClass(classId) {
  return request({
    url: '/school/class/' + classId,
    method: 'get'
  })
}

// 新增开课
export function addClass(data) {
  return request({
    url: '/school/class',
    method: 'post',
    data: data
  })
}

// 修改开课
export function updateClass(data) {
  return request({
    url: '/school/class',
    method: 'put',
    data: data
  })
}

// 删除开课
export function delClass(classId) {
  return request({
    url: '/school/class/' + classId,
    method: 'delete'
  })
}

//随机抽签剔除超员学生
export function executeRandomKick() {
  return request({
    url:'/school/class/randomKick',
    method:'post'
  })
}
