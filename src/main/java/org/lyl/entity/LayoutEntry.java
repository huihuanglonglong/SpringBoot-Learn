package org.lyl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教育中心数据适配收集实体类
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutEntry {

    // 是否收藏
    private Boolean isFavorite;

    // 是否已添加到学习计划
    private Boolean scheduled;

    // 当前课程的订购状态
    private Integer orderState;

    // 当前主修课程
    private String majorCourse;

}
