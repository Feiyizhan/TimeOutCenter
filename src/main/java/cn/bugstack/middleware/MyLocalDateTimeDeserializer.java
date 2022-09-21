/**
 * 
 */
package cn.bugstack.middleware;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 自定义的LocalDateTime反系列化处理器
 * @author 徐明龙 XuMingLong
 * @createDate 2018-04-25
 */
public class MyLocalDateTimeDeserializer extends LocalDateTimeDeserializer {
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static final MyLocalDateTimeDeserializer INSTANCE = new MyLocalDateTimeDeserializer();

    protected MyLocalDateTimeDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public MyLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
            return _fromString(parser, context, parser.getText());
        }
        // 30-Sep-2020, tatu: New! "Scalar from Object" (mostly for XML)
        if (parser.isExpectedStartObjectToken()) {
            return _fromString(parser, context,
                context.extractScalarFromObject(parser, this, handledType()));
        }
        if (parser.isExpectedStartArrayToken()) {
            JsonToken t = parser.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return null;
            }
            if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT)
                && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                final LocalDateTime parsed = deserialize(parser, context);
                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    handleMissingEndArrayForSingle(parser, context);
                }
                return parsed;
            }
            if (t == JsonToken.VALUE_NUMBER_INT) {
                LocalDateTime result;

                int year = parser.getIntValue();
                int month = parser.nextIntValue(-1);
                int day = parser.nextIntValue(-1);
                int hour = parser.nextIntValue(-1);
                int minute = parser.nextIntValue(-1);

                t = parser.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    result = LocalDateTime.of(year, month, day, hour, minute);
                } else {
                    int second = parser.getIntValue();
                    t = parser.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        result = LocalDateTime.of(year, month, day, hour, minute, second);
                    } else {
                        int partialSecond = parser.getIntValue();
                        if (partialSecond < 1_000 &&
                            !context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS))
                            partialSecond *= 1_000_000; // value is milliseconds, convert it to nanoseconds
                        if (parser.nextToken() != JsonToken.END_ARRAY) {
                            throw context.wrongTokenException(parser, handledType(), JsonToken.END_ARRAY,
                                "Expected array to end");
                        }
                        result = LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                    }
                }
                return result;
            }
            context.reportInputMismatch(handledType(),
                "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT",
                t);
        }
        if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return (LocalDateTime) parser.getEmbeddedObject();
        }
        //支持毫秒转LocalDateTime
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            long longValue = parser.getLongValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), UtilsConstant.UTC_8);
        }
        return _handleUnexpectedToken(context, parser, "Expected array or string.");
    }

    @Override
    protected LocalDateTime _fromString(JsonParser p, DeserializationContext ctxt,
        String string0)  throws IOException
    {
        String string = string0.trim();
        if (string.length() == 0) {
            // 22-Oct-2020, tatu: not sure if we should pass original (to distinguish
            //   b/w empty and blank); for now don't which will allow blanks to be
            //   handled like "regular" empty (same as pre-2.12)
            return _fromEmptyString(p, ctxt, string);
        }
        try {
            // 21-Oct-2020, tatu: Changed as per [modules-base#94] for 2.12,
            //    had bad timezone handle change from [modules-base#56]
            if (_formatter == DEFAULT_FORMATTER) {
                // ... only allow iff lenient mode enabled since
                // JavaScript by default includes time and zone in JSON serialized Dates (UTC/ISO instant format).
                // And if so, do NOT use zoned date parsing as that can easily produce
                // incorrect answer.
                if (string.length() > 10 && string.charAt(10) == 'T') {
                    if (string.endsWith("Z")) {
                        if (isLenient()) {
                            return LocalDateTime.parse(string.substring(0, string.length()-1),
                                _formatter);
                        }
                        JavaType t = getValueType(ctxt);
                        return (LocalDateTime) ctxt.handleWeirdStringValue(t.getRawClass(),
                            string,
                            "Should not contain offset when 'strict' mode set for property or type (enable 'lenient' handling to allow)"
                        );
                    }
                }
            }
            int length = string.length();
            switch (length) {
                case 7:
                    return LocalDateTime.parse(string, UtilsConstant.FORMAT_YEAR_MONTH_SLASH);
                case 10:
                    return LocalDateTime.parse(string, UtilsConstant.FORMAT_DATE);
                case 16:
                    return LocalDateTime.parse(string, UtilsConstant.FORMAT_DATE_SHORT_TIME);
                case 19:
                    return LocalDateTime.parse(string, UtilsConstant.FORMAT_DATE_TIME);
                case 23:
                    return LocalDateTime.parse(string, UtilsConstant.FORMAT_TIMESTAMP);
                default:
                    return LocalDateTime.parse(string, UtilsConstant.FORMAT_TIMESTAMP_ALL);
            }
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, e, string);
        }
    }

}
