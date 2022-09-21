/**
 *
 */
package cn.bugstack.middleware;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 系统常量
 * @author 徐明龙 XuMingLong
 * @createDate 2018-04-09
 */
public interface UtilsConstant {



    /**
     * 时区id-东八区
     * @author 徐明龙 XuMingLong
     * @createDate 2018-04-23
     */
    public static final ZoneId  UTC_8 = ZoneId.of("UTC+8");

    /**
     * 时区id-东零区
     * @author 徐明龙 XuMingLong 2020-04-30
     */
    public static final ZoneId  UTC_0 = ZoneId.of("UTC+0");

    /**
     * 日期格式器-年月，例如：2018/04
     * @author 徐明龙 XuMingLong
     * @createDate 2018-10-25
     */
    public static final DateTimeFormatter SIMPLE_FORMAT_DATE = new DateTimeFormatterBuilder().appendPattern("yyyy/MM")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 1)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .toFormatter();

    /**
     * 日期格式器-年月，例如：2018年04月
     * @author wangpeipei
     * @date 2019-05-14 15:49
     */
    public static final DateTimeFormatter SIMPLE_FORMAT_YEAR_MONTH = new DateTimeFormatterBuilder().appendPattern("yyyy年MM月")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 1)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .toFormatter();

    /**
     * 日期格式器-年月日，例如：2018年04月02日
     * @author 徐明龙 XuMingLong 2020-06-09
     */
    public static final DateTimeFormatter SIMPLE_FORMAT_YEAR_MONTH_DAY = new DateTimeFormatterBuilder().appendPattern("yyyy年MM月dd日")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 1)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .toFormatter();

    /**
     * 日期格式器-年月日 时:分，例如：2018年04月02日 13:40
     * @author 徐明龙 XuMingLong 2020-06-09
     */
    public static final DateTimeFormatter SIMPLE_FORMAT_YEAR_MONTH_DAY_HOURS_MINUTE = new DateTimeFormatterBuilder().appendPattern("yyyy年MM月dd日 HH:mm")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 1)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .toFormatter();

    /**
     * 日期格式器-年月日，例如：2018/04/18
     * @author 徐明龙 XuMingLong
     * @createDate 2018-04-18
     */
    public static final DateTimeFormatter FORMAT_DATE = new DateTimeFormatterBuilder()
            .appendPattern("yyyy/MM/dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
            .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter()
            .withZone(UTC_8);

    /**
     * 日期格式器-时分秒，例如：12:15:18
     * @author 徐明龙 XuMingLong 2019-02-21
     */
    public static final DateTimeFormatter FORMAT_TIME = new DateTimeFormatterBuilder()
        .appendPattern("HH:mm:ss")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式器-时分，例如：12:15
     * @author 徐明龙 XuMingLong 2019-02-21
     */
    public static final DateTimeFormatter FORMAT_SHORT_TIME = new DateTimeFormatterBuilder()
        .appendPattern("HH:mm")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式器-年月日时分秒，例如2018/04/18 15:08:19
     * @author 徐明龙 XuMingLong
     * @createDate 2018-04-18
     */
    public static final DateTimeFormatter FORMAT_DATE_TIME =  new DateTimeFormatterBuilder()
            .appendPattern("yyyy/MM/dd HH:mm:ss")
            .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
            .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter()
            .withZone(UTC_8);

    /**
     * 日期格式器-年月日时分，例如2018/04/18 15:08
     * @author 徐明龙 XuMingLong
     * @createDate 2018-04-18
     */
    public static final DateTimeFormatter FORMAT_DATE_SHORT_TIME =  new DateTimeFormatterBuilder()
            .appendPattern("yyyy/MM/dd HH:mm")
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
            .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter()
            .withZone(UTC_8);


    /**
     * 日期格式器-年月日时分秒毫秒，例如2018/04/18 15:08:19.156
     * @author 徐明龙 XuMingLong
     * @createDate 2018-04-18
     */
    public static final DateTimeFormatter FORMAT_TIMESTAMP = new DateTimeFormatterBuilder()
        .appendPattern("yyyy/MM/dd HH:mm:ss.SSS")
        .toFormatter()
        .withZone(UTC_8);
    /**
     * 日期格式器-年月日无分隔符，例如20180418
     * @author 徐明龙 XuMingLong 2019-04-08
     */
    public static final DateTimeFormatter FORMAT_YEAR_MONTH_DAY = new DateTimeFormatterBuilder()
        .appendPattern("yyyyMMdd")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式器-年月，例如2018-04
     * @author 徐明龙 XuMingLong 2019-04-08
     */
    public static final DateTimeFormatter FORMAT_YEAR_MONTH_MINUS = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式器-年月日，例如2018-04-01
     * @author 徐明龙 XuMingLong 2019-04-08
     */
    public static final DateTimeFormatter FORMAT_YEAR_MONTH_DAY_MINUS = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式器-年月，例如201804
     * @author 徐明龙 XuMingLong 2019-05-08
     */
    public static final DateTimeFormatter FORMAT_YEAR_MONTH = new DateTimeFormatterBuilder()
        .appendPattern("yyyyMM")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 带斜杠的年月
     * @author wangpeipei
     * @date 2019-04-29 11:16
     */
    public static final DateTimeFormatter FORMAT_YEAR_MONTH_SLASH = new DateTimeFormatterBuilder()
        .appendPattern("yyyy/MM")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式化器-年
     * @author 徐明龙 XuMingLong 2019-05-14
     * @return
     */
    public static final DateTimeFormatter FORMAT_YEAR = new DateTimeFormatterBuilder()
        .appendPattern("yyyy")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 1)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(UTC_8);

    /**
     * 日期格式化器-月日
     * @author 徐明龙 XuMingLong 2019-05-14
     * @return
     */
    public static final DateTimeFormatter FORMAT_MONTH_DAY = new DateTimeFormatterBuilder()
        .appendPattern("MM-dd")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 1)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(UTC_8);


    /**
     * 日期格式器-年月日时分秒毫秒微秒纳秒，例如2018/04/18 15:08:19.156000567
     * @author 徐明龙 XuMingLong
     * @createDate 2018-04-18
     */
    public static final DateTimeFormatter FORMAT_TIMESTAMP_ALL = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSSSSS").withZone(UTC_8);

    /**
     * 日期格式：yyyyMMddHHmmss
     * @author 徐明龙 XuMingLong
     * @createDate 2018-06-28
     */
    public static final DateTimeFormatter FORMAT_DAY_SECOND_JOIN = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(UTC_8);


    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     * @author 徐明龙 XuMingLong
     * @createDate 2018-08-23
     */
    public static final DateTimeFormatter FORMAT_STANDARD_DATE_TIME_SECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(UTC_8);

    /**
     * 日期格式：yyyyMMddHHmmssSSSSSSSSS
     * @author 徐明龙 XuMingLong
     * @createDate 2020-09-11
     */
    public static final DateTimeFormatter FORMAT_DAY_SECOND_NANO_JOIN = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSSSS").withZone(UTC_8);

    /**
     * 水印图片文件类型
     * @author 徐明龙 XuMingLong
     * @createDate 2018-07-06
     */
    public static final String WATERMARK_IMAGE_FILE_TYPE="png";

    /**
     * 水印图片-内容类型
     * @author 徐明龙 XuMingLong
     * @createDate 2018-07-06
     */
    public static final String WATERMARK_IMAGE_CONTENT_TYPE="image/png";


    /**
     * 字符编码
     * @author 徐明龙 XuMingLong 2019-08-30
     */
    public static final String CHARSET_NAME = StandardCharsets.UTF_8.name();


}
