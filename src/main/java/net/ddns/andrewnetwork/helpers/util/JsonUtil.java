package net.ddns.andrewnetwork.helpers.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateDeserializer;
import net.ddns.andrewnetwork.helpers.util.time.DateSerializer;

import java.util.Date;

public final class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .registerTypeAdapter(Date.class, new DateSerializer())
            .create();


    public static Gson getGson() {
        return gson;
    }
}
