package org.lyl.common.util.encrypt;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.google.common.collect.Sets;
import io.lettuce.core.StrAlgoArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

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


    public static void main(String[] args) {
        String url = "https://localhost:8000/test1?key5=val5&key4=val4&key1=val1&key2=val2&key3=val3";

        Map<String, Object> paramMap = getParamMapByUrl(url);
        System.out.println(paramMap);

        String sortedParamStr = buildSortedParamStr(paramMap);
        System.out.println("sortedParamStr = " +sortedParamStr);
    }





}
