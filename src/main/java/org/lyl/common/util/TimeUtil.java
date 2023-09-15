package org.lyl.common.util;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimeUtil {

    public static void main(String[] args) {
        //testList();
        testDateFormat();
    }

    public static void testList() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        Integer[] intArray = intList.toArray(new Integer[]{});

        intList = Arrays.stream(intArray).collect(Collectors.toList());
        System.out.println(intList);

        intList = Lists.newArrayList(intArray);
        System.out.println(intList);

        intList = Arrays.asList(intArray);
        intList.add(5);
        System.out.println(intList);

        List<Integer> dataList = Lists.newArrayList();
        Stream.iterate(0, i -> i+2).limit(200).forEach(x -> dataList.add(x));
    }

    public static void testDateFormat() {
        /*LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateFormat = now.format(dtf);*/
        int i = 1;
        int n = i++;
        System.out.println("n = "+ n);
        int m = i++;
        System.out.println("m = "+ m);


    }

}
