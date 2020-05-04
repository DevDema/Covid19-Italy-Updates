package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ConfigData {

    @SerializedName("last_seen_date")
    private Date date;
    @SerializedName("channel_id")
    private long channelID;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getChannelID() {
        return channelID;
    }
}
