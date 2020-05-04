package net.ddns.andrewnetwork.helpers.util.time;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.ddns.andrewnetwork.helpers.util.time.DateUtil.DATE_TIME_FORMAT;

public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();

        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);

        try {
            return format.parse(date);
        } catch (ParseException exp) {
            System.err.println(exp.getMessage());
            return null;
        }
    }
}