package net.ddns.andrewnetwork.helpers.util.builder;

import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.FileUtils;
import net.ddns.andrewnetwork.helpers.util.JsonUtil;
import net.ddns.andrewnetwork.model.ConfigData;
import net.ddns.andrewnetwork.model.ConfigSavedData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.Collection;
import java.util.Date;

public class ConfigDataBuilder {

    private static String CONFIG_PATH = "covid19Updates.config";
    private static final ConfigDataBuilder configHelper = new ConfigDataBuilder();
    private ConfigData configData;

    public ConfigDataBuilder putDays(Collection<ConfigSavedData> savedData) {
        if(configData == null) {
            throw new IllegalStateException("putDays() called without calling getData() first.");
        }

        configData.setLastDays(savedData);

        return this;
    }

    public ConfigDataBuilder putMessageId(long messageId) {
        if(configData == null) {
            throw new IllegalStateException("putMessageId() called without calling getData() first.");
        }

        configData.setMessageID(messageId);

        return this;
    }



    public ConfigDataBuilder putChannelId(long channelId) {
        if(configData == null) {
            throw new IllegalStateException("putChannelId() called without calling getData() first.");
        }

        configData.setChannelID(channelId);

        return this;
    }

    private ConfigDataBuilder newData() {
        this.configData = new ConfigData();

        return this;
    }
    public ConfigDataBuilder getData() {
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

    public static void clear() {
        ConfigData configData1 = new ConfigData();

        ConfigDataBuilder.getInstance()
                .newData()
                .putChannelId(configData1.getChannelID())
                .commit();
    }

    public static String getConfigDataString()  {
        return FileUtils.getFile(CONFIG_PATH);
    }

    public static ConfigData getConfigData() {
        String fileAsString = getConfigDataString();

        return JsonUtil.getGson().fromJson(fileAsString, ConfigData.class);
    }

    public static void setConfigPath(String configPath) {
        CONFIG_PATH = configPath;
    }

    public static ConfigDataBuilder getInstance() {
        return configHelper;
    }


}
