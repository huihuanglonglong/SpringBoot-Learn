package org.lyl.generic;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.TypeReference;

import java.util.Map;

public class GenericArray<T> {
    private T[] array;

    public GenericArray(int sz) {
        array = (T[])new Object[sz];   // 创建泛型数组
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) { return array[index]; }

    // Method that exposes the underlying representation:
    public T[] rep() { return array; }     //返回数组 会报错

    private static final Map<String, Integer> dataMap =
        JSON.parseObject("json", new TypeReference<Map<String, Integer>>(){});


    public static void main(String[] args) {
        GenericArray<Integer> gai = new GenericArray<Integer>(10);
        // This causes a ClassCastException:
        //! Integer[] ia = gai.rep();

        // This is OK:
        Object[] oa = gai.rep();

    }
}
