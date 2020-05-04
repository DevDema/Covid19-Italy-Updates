package net.ddns.andrewnetwork.helpers;

import net.ddns.andrewnetwork.helpers.util.FileUtils;
import net.ddns.andrewnetwork.helpers.util.JsonUtil;
import net.ddns.andrewnetwork.model.ConfigData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.Date;
import java.util.List;

public class ConfigHelper {

    private static String CONFIG_PATH = "covid19Updates.config";
    private static ConfigHelper configHelper = new ConfigHelper();
    private ConfigData configData;


    public static String getConfigDataString()  {
        return FileUtils.getFile(CONFIG_PATH);
    }

    public static ConfigData getConfigData() {
        String fileAsString = getConfigDataString();

        return JsonUtil.getGson().fromJson(fileAsString, ConfigData.class);
    }

    public ConfigHelper putDate(Date date) {
        if(configData == null) {
            throw new IllegalStateException("putDate() called without calling getData() first.");
        }

        configData.setDate(date);

        return this;
    }

    public ConfigHelper putTodayData(CovidItaData covidItaData, List<CovidRegionData> covidRegionDataList) {
        if(configData == null) {
            throw new IllegalStateException("putDate() called without calling getData() first.");
        }

        configData.setItalyDataSaved(covidItaData);
        configData.setRegionsDataSaved(covidRegionDataList);

        return this;
    }

    public ConfigHelper getData() {
        this.configData = getConfigData();

        return this;
    }

    public void commit() {
        if(configData == null) {
            throw new IllegalStateException("commit() called without calling getData() first.");
        }

        FileUtils.writeFile(CONFIG_PATH, JsonUtil.getGson().toJson(configData));

        configData = null;
    }

    public static void setConfigPath(String configPath) {
        CONFIG_PATH = configPath;
    }

    public static ConfigHelper getInstance() {
        return configHelper;
    }
}
