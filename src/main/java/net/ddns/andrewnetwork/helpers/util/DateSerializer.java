package net.ddns.andrewnetwork.helpers.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static net.ddns.andrewnetwork.helpers.util.DateUtil.DATE_TIME_FORMAT;

public class DateSerializer implements JsonSerializer<Date> {

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);

        return new JsonPrimitive(format.format(date));
    }
}
