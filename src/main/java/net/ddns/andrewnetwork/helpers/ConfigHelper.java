package net.ddns.andrewnetwork.helpers;

import net.ddns.andrewnetwork.helpers.util.FileUtils;
import net.ddns.andrewnetwork.helpers.util.JsonUtil;
import net.ddns.andrewnetwork.model.ConfigData;

import java.util.Date;

public class ConfigHelper {

    private static final String CONFIG_PATH = "covid19Updates.config";

    public static String getConfigDataString() {
        return FileUtils.getFile(CONFIG_PATH);
    }
    public static ConfigData getConfigData() {
        String fileAsString = getConfigDataString();

        return JsonUtil.getGson().fromJson(fileAsString, ConfigData.class);
    }

    public static void putRowData(Date date) {
        ConfigData configData = getConfigData();

        configData.setDate(date);

        FileUtils.writeFile(CONFIG_PATH, JsonUtil.getGson().toJson(configData));
    }
}
