package org.lyl.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class DateTimeUtil {

    public static final String STANDARD_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String STANDARD_MILLIS_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";

    public static final String STANDARD_MONTH_FORMAT = "yyyy-MM";

    public static final String STANDARD_UTC_FORMAT = "yyy-MM-dd'T'HH:mm:ss:SSS'Z'";

    public static final String NUM_SECOND_FORMAT = "yyyyMMddHHmmss";

    public static final String NUM_MILLIS_FORMAT = "yyyyMMddHHmmssSSS";

    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private static final Map<String, DateTimeFormatter> TIME_FORMATTER_MAP = Maps.newHashMap();

    static {
        TIME_FORMATTER_MAP.put(STANDARD_TIME_FORMAT, DateTimeFormatter.ofPattern(STANDARD_TIME_FORMAT));
        TIME_FORMATTER_MAP.put(STANDARD_MILLIS_FORMAT, DateTimeFormatter.ofPattern(STANDARD_MILLIS_FORMAT));
        TIME_FORMATTER_MAP.put(STANDARD_DATE_FORMAT, DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT));
        TIME_FORMATTER_MAP.put(STANDARD_MONTH_FORMAT, DateTimeFormatter.ofPattern(STANDARD_MONTH_FORMAT));
        TIME_FORMATTER_MAP.put(STANDARD_UTC_FORMAT, DateTimeFormatter.ofPattern(STANDARD_UTC_FORMAT));
        TIME_FORMATTER_MAP.put(NUM_SECOND_FORMAT, DateTimeFormatter.ofPattern(NUM_SECOND_FORMAT));
        TIME_FORMATTER_MAP.put(NUM_MILLIS_FORMAT, DateTimeFormatter.ofPattern(NUM_MILLIS_FORMAT));
    }

    public static String getCurrentTimeByFormat(String format) {
        return LocalDateTime.now().format(TIME_FORMATTER_MAP.get(format));
    }

    // 获取当前时区的Date
    public static Date getCurrentZoneDate() {
        return Date.from(LocalDateTime.now().atZone(DEFAULT_ZONE_ID).toInstant());
    }

    // 湖区当前系统的标准时间
    public static String getCurrentStandardTime() {
        return LocalDateTime.now().format(TIME_FORMATTER_MAP.get(STANDARD_TIME_FORMAT));
    }

    // 获取当前系统时区下标准毫秒时间
    public static String getCurrentStandardMillisTime() {
        return LocalDateTime.now().format(TIME_FORMATTER_MAP.get(STANDARD_MILLIS_FORMAT));
    }

    // str ---> Date
    public static Date getDateFromTimeStr(String dateTime, String dateTimeFormat) {
        Date resultDate = null;
        try {
            resultDate = new SimpleDateFormat(dateTimeFormat).parse(dateTime);
        } catch (Exception e) {
            log.error("getDateFromTimeStr have error------>", e);
        }
        return resultDate;
    }

    // 获取当前系统0点时间
    public static String getCurrentStandardZeroTime() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        return localDateTime.format(TIME_FORMATTER_MAP.get(STANDARD_TIME_FORMAT));
    }

    // 获取与当前时间相差diff天，
    public static String getCurrentDiffDay(int diff, String format) {
        return LocalDateTime.now().plusDays(diff).format(TIME_FORMATTER_MAP.get(format));
    }

    // 获取与当前时间相差diff天的零点时间
    public static String getDiffDayZeroTime(int diff, String format) {
        return LocalDate.now().plusDays(diff).atStartOfDay().format(TIME_FORMATTER_MAP.get(format));
    }


    // 获取两个统一时间格式范围类的月份列表，包含起始月
    public static List<String> getYearMonthList(Date start, Date end, String format) {
        List<String> diffMonth = Lists.newArrayList();
        LocalDateTime startLocal = date2ZonedDateTime(start, DEFAULT_ZONE_ID).toLocalDateTime().withDayOfMonth(1).with(LocalTime.of(0, 0, 0));
        LocalDateTime endLocal = date2ZonedDateTime(end, DEFAULT_ZONE_ID).toLocalDateTime().withDayOfMonth(1).with(LocalTime.of(0, 0, 0));
        long diff = ChronoUnit.MONTHS.between(startLocal, endLocal);
        Stream.iterate(0, i -> i + 1).limit(diff)
            .forEach(i -> diffMonth.add(startLocal.plusMonths(i).format(TIME_FORMATTER_MAP.get(format))));
        return diffMonth;
    }

    // 时间串 转 Date (Date 与 LocalXXX、ZonedDateTime之间的转换必须要经过Instance)
    public static Date dateStr2Date(String dateStr, String format) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, TIME_FORMATTER_MAP.get(format));
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    public static Date zonedDateTime2Date(ZonedDateTime zonedTime, ZoneId zoneId) {
        zoneId = Objects.isNull(zoneId)? DEFAULT_ZONE_ID : zoneId;
        LocalDateTime localDateTime = zonedTime.toLocalDateTime();
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static Date localTime2Date(LocalDateTime localDateTime, ZoneId zoneId) {
        zoneId = Objects.isNull(zoneId)? DEFAULT_ZONE_ID : zoneId;
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static Date localDate2Date(LocalDate localDate, ZoneId zoneId) {
        return localTime2Date(localDate.atStartOfDay(), zoneId);
    }



    /***************************** UTC时间 *********************************/

    // Date 转ZonedDateTime
    public static ZonedDateTime date2ZonedDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId);
    }

    // 获取系统当前UTC时间格式
    public static String getCurrentUTCTimeStr() {
        return ZonedDateTime.now(Clock.systemUTC()).format(TIME_FORMATTER_MAP.get(STANDARD_UTC_FORMAT));
    }

    // 指定Date和zoneId获取UTC时间
    public static String date2UTCTimeStr(Date date) {
        return date.toInstant().atZone(ZoneId.of("UTC")).format(TIME_FORMATTER_MAP.get(STANDARD_UTC_FORMAT));
    }

    // UTC 时间转成本地时区时间 (先转Instant, 再转LocalDateTime)
    public static String utcTimeStr2LocalTime(String utcTimeStr, String utcFormat, String toFormat) {
        ZonedDateTime zonedDateTime = LocalDateTime.parse(utcTimeStr, TIME_FORMATTER_MAP.get(utcFormat)).atZone(DEFAULT_ZONE_ID);
        return zonedDateTime.toLocalDateTime().format(TIME_FORMATTER_MAP.get(toFormat));
    }





    // LocalDateTime
    // 参考：https://blog.csdn.net/qq_42582773/article/details/127554933
    // https://blog.csdn.net/weixin_55365140/article/details/118219902
    // https://blog.csdn.net/TreeShu321/article/details/100183584

    /**
     *         String zeroTime = getCurrentStandardZeroTime();
     *         System.out.println("zeroTime = " + zeroTime);
     *
     *         zeroTime = getCurrentDiffDay(-9, DateTimeUtil.STANDARD_MONTH_FORMAT);
     *         System.out.println("current diff day = " + zeroTime);
     *
     *         zeroTime = getDiffDayZeroTime(-90, DateTimeUtil.STANDARD_TIME_FORMAT);
     *         System.out.println("current diff time = " + zeroTime);
     *
     *         zeroTime = getCurrentDiffDay(-90, DateTimeUtil.STANDARD_TIME_FORMAT);
     *         Date startDate = strToDateByFormat(zeroTime, DateTimeUtil.STANDARD_TIME_FORMAT);
     *         List<String> monthList = getYearMonthList(startDate, new Date(), DateTimeUtil.STANDARD_MONTH_FORMAT);
     *         monthList.forEach(System.out::println);
     *
     *         String utcTimeStr = getCurrentUTCTimeStr();
     *         System.out.println("utcTimeStr = " + utcTimeStr );
     *         String localMilliTime = getCurrentStandardMillisTime();
     *         System.out.println("localMilliTime = " + localMilliTime);
     *
     *         String localTime = utcTimeStr2LocalTime(utcTimeStr, DateTimeUtil.STANDARD_UTC_FORMAT, DateTimeUtil.STANDARD_MILLIS_FORMAT);
     *         System.out.println("localTime = " + localTime);
     *
     *         utcTimeStr = date2UTCTimeStr(new Date());
     *         System.out.println("current date2UTCTimeStr = " + utcTimeStr);
     *
     * @param args
     */
    public static void main(String[] args) {
        String str = "Hello?World&Java";
        String[] result = str.split("\\?"); // 注意需要转义"&"为"\&"
        for (int i=0; i<result.length; i++) {
            System.out.println(result[i]);
        }
        Date date = getDateFromTimeStr("2024-06-16", DateTimeUtil.STANDARD_DATE_FORMAT);
        System.out.println("date = " + date);
    }

}
