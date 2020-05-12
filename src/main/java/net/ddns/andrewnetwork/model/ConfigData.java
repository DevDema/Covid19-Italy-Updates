package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

import java.util.*;

public class ConfigData {


    @SerializedName("channel_id")
    private long channelID;
    @SerializedName("last_message_id")
    private long messageID;
    @SerializedName("last_days")
    private final List<ConfigSavedData> lastDays = new ArrayList<>();

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

    public List<ConfigSavedData> getLastDays() {
        return lastDays;
    }
}
