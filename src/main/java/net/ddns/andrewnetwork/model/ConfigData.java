package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ConfigData {


    @SerializedName("channel_id")
    private long channelID;
    @SerializedName("last_message_id")
    private long messageID;
    @SerializedName("last_days")
    private Collection<ConfigSavedData> lastDays = new ArrayList<>();

    public long getChannelID() {
        return channelID;
    }

    public void setChannelID(long channelID) {
        this.channelID = channelID;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public Collection<ConfigSavedData> getLastDays() {
        return lastDays;
    }

    public void setLastDays(Collection<ConfigSavedData> lastDays) {
        this.lastDays = lastDays;
    }

    public ConfigSavedData getLastDay() {
        Collection<ConfigSavedData> lastDays = getLastDays();

        return CovidDataUtils.getMostRecentDate(lastDays);
    }

    public ConfigSavedData getDayBy(Date date) {
        Collection<ConfigSavedData> lastDays = getLastDays();

        return lastDays
                .stream()
                .filter(configSavedData -> configSavedData.getDate() != null && DateUtil.isSameDay(configSavedData.getDate(), date))
                .findFirst()
                .orElse(null);
    }
}
