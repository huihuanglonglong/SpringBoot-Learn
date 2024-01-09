package org.lyl.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {


    /**
     * 列表按照分页的方式进行分组
     *
     * @param dataList
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partitionByPage(List<T> dataList, int batch) {
        return Lists.partition(dataList, batch);
    }

    /**
     * 使用Stream的方式将列表进行分组
     *
     * @param dataList
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partitionByStream(List<T> dataList, int batch) {
        List<List<T>> partitionList = Lists.newArrayList();
        int maxPageNum = (dataList.size() % batch == 0) ? (dataList.size() / batch) : ((dataList.size() / batch) + 1);
        for (int i = 0; i < maxPageNum; i++) {
            partitionList.add(dataList.stream().skip(i * batch).limit(batch).collect(Collectors.toList()));
        }
        return partitionList;
    }


    /*public static void main(String[] args) {
        List<Integer> dataList = Lists.newArrayList(1, 3, 5, 7, 9, 11, 13, 15, 17, 20);
        List<List<Integer>> partitionList = partitionByStream(dataList, 3);
        partitionList.forEach(System.out::println);
    }*/


}
