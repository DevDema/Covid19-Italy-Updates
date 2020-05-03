package net.ddns.andrewnetwork.helpers.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public final class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();


    public static Gson getGson() {
        return gson;
    }
}
