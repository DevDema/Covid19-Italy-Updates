package net.ddns.andrewnetwork.helpers.util.builder;

import net.ddns.andrewnetwork.helpers.util.FileUtils;
import net.ddns.andrewnetwork.helpers.util.JsonUtil;
import net.ddns.andrewnetwork.model.ConfigData;
import net.ddns.andrewnetwork.model.TemporaryConfigData;

public class ConfigDataBuilder {

    private static final ConfigDataBuilder configDataHelper = new ConfigDataBuilder();
    private static String CONFIG_PATH;
    private ConfigData configData;

    public static void clearTemporaryData() {
        ConfigDataBuilder.getInstance()
                .getData()
                .putTemporaryData(new TemporaryConfigData())
                .commit();
    }

    public static String getConfigDataString() {
        if (CONFIG_PATH == null || CONFIG_PATH.isEmpty()) {
            throw new IllegalArgumentException("Accessing CONFIG_PATH without setting it first.");
        }

        return FileUtils.getFile(CONFIG_PATH);
    }

    public static ConfigData getConfigData() {
        String fileAsString = getConfigDataString();

        return JsonUtil.getGson().fromJson(fileAsString, ConfigData.class);
    }

    public static long getMessageId() {
        ConfigData configData = getConfigData();

        if (configData.getTemporaryData() == null) {
            return 0;
        }

        return configData.getTemporaryData().getMessageID();
    }

    public static String getConfigPath() {
        return CONFIG_PATH;
    }

    public static ConfigDataBuilder getInstance() {
        return configDataHelper;
    }

    public ConfigDataBuilder putMessageId(long messageId) {
        if (configData == null) {
            throw new IllegalStateException("putMessageId() called without calling getData() first.");
        }

        TemporaryConfigData temporaryConfigData = configData.getTemporaryData();
        if (temporaryConfigData == null) {
            temporaryConfigData = new TemporaryConfigData();
        }

        temporaryConfigData.setMessageID(messageId);

        return this;
    }

    public ConfigDataBuilder putChannelId(long channelId) {
        if (configData == null) {
            throw new IllegalStateException("putChannelId() called without calling getData() first.");
        }

        configData.setChannelID(channelId);

        return this;
    }

    public ConfigDataBuilder putTemporaryData(TemporaryConfigData temporaryConfigData) {
        if (configData == null) {
            throw new IllegalStateException("putTemporaryData() called without calling getData() first.");
        }

        configData.setTemporaryConfigData(temporaryConfigData);

        return this;
    }

    public static void setConfigPath(String configPath) {
        CONFIG_PATH = configPath;
    }

    public ConfigDataBuilder getData() {
        this.configData = getConfigData();

        return this;
    }

    public void commit() {
        if (CONFIG_PATH == null || CONFIG_PATH.isEmpty()) {
            throw new IllegalArgumentException("Accessing CONFIG_PATH without setting it first.");
        }

        if (configData == null) {
            throw new IllegalStateException("commit() called without calling getData() first.");
        }

        FileUtils.writeFile(CONFIG_PATH, JsonUtil.getGson().toJson(configData));

        configData = null;
    }

}
