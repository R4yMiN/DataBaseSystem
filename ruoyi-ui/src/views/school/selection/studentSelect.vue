handleSelect() {
// this.ids 存的是学生勾选的多个 classId
const classIds = this.ids;
// 调用专门的选课接口
request({
url: '/school/selection/batchAdd',
method: 'post',
data: classIds
}).then(res => {
this.$modal.msgSuccess("选课申请已提交");
});
}
