import request from '@/utils/request'

// 学生查询自己的成绩
export function getMyGrades(query) {
  return request({
    url: '/school/selection/myGrades',
    method: 'get',
    params: query
  })
}
