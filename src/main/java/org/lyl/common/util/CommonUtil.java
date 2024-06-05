package org.lyl.common.util;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.lettuce.core.StrAlgoArgs;
import io.reactivex.Completable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.lyl.constant.DataOpTypeEnum;
import org.lyl.entity.UserInfo;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtil {


    public static final Set<Class<?>> BASIC_CLASS = Sets.newHashSet(
        byte.class, short.class, char.class, int.class, long.class, float.class, double.class, boolean.class,
        Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class
    );


    public static boolean isBasicClazz(Class<?> clazz) {
        return BASIC_CLASS.contains(clazz);
    }

    public static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("jdk md5 have error------>",e);
        }
        return res;
    }


    public static String sha256(String src) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(src.getBytes(StandardCharsets.UTF_8));
            byte[] rtnByte = messageDigest.digest();

            for (byte b : rtnByte) {
                sb.append(String.format("%02X", b));
            }
        } catch (Exception e) {
            log.error("jdk sha256 have error------>",e);
        }
        return sb.toString();
    }


    /**
     * 从rul中构建参数map
     *
     * @param
     * @return
     */
    public static Map<String, Object> getParamMapByUrl(String url) {
        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isBlank(url)) {
            return paramMap;
        }

        String[] urlArray = url.split("\\?");
        if (urlArray.length == 1 && !urlArray[0].contains("=")) {
            return paramMap;
        }

        String paramUri = (urlArray.length == 1 && urlArray[0].contains("="))? urlArray[0] : urlArray[1];
        String[] keyValuePairs = paramUri.split("\\&");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split("=", 2);
            paramMap.put(keyValue[0], keyValue[1]);
        }
        return paramMap;
    }


    public static String buildSortedParamStr(Map params) {
        if (MapUtils.isEmpty(params)) {
            return StringUtils.EMPTY;
        }
        params.remove(null);
        if (MapUtils.isEmpty(params)) {
            return StringUtils.EMPTY;
        }

        TreeMap<String, String> tempMap = new TreeMap<>();
        params.forEach((key, value) -> tempMap.put((String) key, Objects.isNull(value)? StringUtils.EMPTY : (String) value));


        StringBuilder buf = new StringBuilder();
        for (String key : tempMap.keySet()) {
            buf.append(key).append("=").append(tempMap.get(key)).append("&");
        }
        return buf.substring(0, buf.length() - 1);
    }


    /**
     *
     * 用户自定义对象属性去重器
     *
     * @param fieldExtractFun
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Predicate<T> distinctBy(Function<T, R> fieldExtractFun) {
        Map<R, Boolean> objFieldExistMap = Maps.newHashMap();
        return sourceObj -> Objects.isNull(objFieldExistMap.putIfAbsent(fieldExtractFun.apply(sourceObj), Boolean.TRUE));
    }


    /**
     * 复杂的集合运算
     * 多重分组，组合之后在计算
     *
     * @param userInfos
     * @return
     */
    public static Map<Date, Map<String, Integer>> multiGroupCompute(List<UserInfo> userInfos) {
        return userInfos.stream().collect(Collectors.groupingBy(UserInfo::getBirthDate,
            Collectors.groupingBy(UserInfo::getAddress, Collectors.collectingAndThen(Collectors.toList(), CommonUtil::sumGroupAmount))));
    }

    private static Integer sumGroupAmount(List<UserInfo> groupUserInfos) {
        return groupUserInfos.stream().mapToInt(UserInfo::getAmount).sum();
    }


    /**
     * 将一个普通的Map 转成一个通过value的Map
     *
     * @param sourceMap
     * @param needReverse
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<V>> Map<K, V> convertMap2ValSortedMap(Map<K, V> sourceMap, boolean needReverse) {
        if (MapUtils.isEmpty(sourceMap)) {
            return Maps.newLinkedHashMap();
        }
        Comparator<V> comparator = needReverse ? Collections.reverseOrder() : Comparator.naturalOrder();
        return sourceMap.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> newVal, LinkedHashMap::new));
    }


    /**
     *
     * 分离两个集合中，新增，删除，修改的数据
     * 不改变原有的集合
     *
     * @param oldCollect
     * @param newCollect
     * @param <T>
     * @return
     */
    public static <T> Map<String, Set<T>> splitCollections(Collection<T> oldCollect, Collection<T> newCollect) {
        Map<String, Set<T>> splitMap = Maps.newHashMap();

        splitMap.put(DataOpTypeEnum.ADD.getOpType(), Sets.newHashSet());
        splitMap.put(DataOpTypeEnum.DELETE.getOpType(), Sets.newHashSet());
        splitMap.put(DataOpTypeEnum.UPDATE.getOpType(), Sets.newHashSet());

        if (!(oldCollect instanceof Set)) {
            oldCollect = Sets.newHashSet(oldCollect);
        }
        if (!(newCollect instanceof Set)) {
            newCollect = Sets.newHashSet(newCollect);
        }

        for (T sourceData : oldCollect) {
            String opType = newCollect.contains(sourceData) ? DataOpTypeEnum.UPDATE.getOpType() : DataOpTypeEnum.DELETE.getOpType();
            Set<T> determineCollect = splitMap.get(opType);
            determineCollect.add(sourceData);
        }

        Set<T> updateList = splitMap.get(DataOpTypeEnum.UPDATE.getOpType());
        log.info("splitCollections update size = {}", updateList.size());

        newCollect.forEach(compareData -> {
            if (!updateList.contains(compareData)) {
                splitMap.get(DataOpTypeEnum.ADD.getOpType()).add(compareData);
            }
        });
        return splitMap;
    }


    public static void main(String[] args) {
        String url = "https://localhost:8000/test1?key5=val5&key4=val4&key1=val1&key2=val2&key3=val3";

        Map<String, Object> paramMap = getParamMapByUrl(url);
        System.out.println(paramMap);

        String sortedParamStr = buildSortedParamStr(paramMap);
        System.out.println("sortedParamStr = " +sortedParamStr);
    }





}
