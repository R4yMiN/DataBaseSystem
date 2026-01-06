package com.ruoyi.school.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.school.domain.StuClass;
import org.apache.ibatis.annotations.Param;

/**
 * 开课Mapper接口
 *
 * @author ruoyi
 * @date 2025-12-30
 */
public interface StuClassMapper
{
    /**
     * 查询开课
     *
     * @param classId 开课主键
     * @return 开课
     */
    public StuClass selectStuClassByClassId(Long classId);

    /**
     * 查询开课列表
     *
     * @param stuClass 开课
     * @return 开课集合
     */
    public List<StuClass> selectStuClassList(StuClass stuClass);

    /**
     * 新增开课
     *
     * @param stuClass 开课
     * @return 结果
     */
    public int insertStuClass(StuClass stuClass);

    /**
     * 检查是否已存在相同的开课记录（用于防止重复提交）
     */
    public int existsDuplicateStuClass(StuClass stuClass);

    /**
     * 修改开课
     *
     * @param stuClass 开课
     * @return 结果
     */
    public int updateStuClass(StuClass stuClass);

    /**
     * 删除开课
     *
     * @param classId 开课主键
     * @return 结果
     */
    public int deleteStuClassByClassId(Long classId);

    /**
     * 批量删除开课 (修复报错的关键方法)
     *
     * @param classIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuClassByClassIds(Long[] classIds);

    /**
     * 执行随机抽签剔除逻辑 (调用存储过程)
     */
    public void executeRandomKick();

    /**
     * 检查排课冲突数量
     *
     * @param stuClass 排课信息
     * @return 冲突数
     */
    public int checkSchedulingConflict(StuClass stuClass);

    /**
     * 增加已选人数 (修改 stu_class 表)
     */
    public int plusSelectedNum(Long classId);

    /**
     * 减少已选人数 (修改 stu_class 表)
     */
    public int minusSelectedNum(Long classId);

    /**
     * 从课程库表里提取所有可选课程
     *
     * @return 课程Map集合
     */
    public List<Map<String, Object>> selectAllCourseOptions();

    /**
     * 从教室表里提取所有教室列表
     *
     * @return 教室Map集合
     */
    public List<Map<String, Object>> selectAllClassroomOptions();

    /**
     * 获取同一课程+学期下主课( is_primary = 1 )已使用的最大班级号数字部分。
     * 例如：01班、02班 -> 返回 2。
     */
    Integer selectMaxPrimaryClassSectionNo(@Param("courseId") String courseId, @Param("semester") String semester);

    /**
     * 检查同一课程+学期下主课班级号是否已存在（用于保证班级号全局唯一）。
     */
    int existsPrimaryClassSection(@Param("courseId") String courseId,
                                 @Param("semester") String semester,
                                 @Param("classSection") String classSection,
                                 @Param("excludeClassId") Long excludeClassId);
}