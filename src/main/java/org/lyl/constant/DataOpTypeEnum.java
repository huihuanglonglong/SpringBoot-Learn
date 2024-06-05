package org.lyl.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataOpTypeEnum {

    ADD("add", "增加"),

    UPDATE("update", "修改"),

    DELETE("delete", "删除"),

    NO_CHANGE("noChange", "无变化");



    private String opType;


    private String opName;



}
