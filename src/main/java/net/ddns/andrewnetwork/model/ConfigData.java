package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

public class ConfigData {

    @SerializedName("channel_id")
    private long channelID;
    @SerializedName("country")
    private String country;
    @SerializedName("daemon_mode")
    private boolean daemonMode;
    @SerializedName("debug_mode")
    private boolean debugMode;
    @SerializedName("regions")
    private String[] regions;
    @SerializedName("language")
    private String language;
    @SerializedName("cycling_interval")
    private int timeInterval;
    @SerializedName("temporary_data")
    private TemporaryConfigData temporaryConfigData;

    public long getChannelID() {
        return channelID;
    }

    public void setChannelID(long channelID) {
        this.channelID = channelID;
    }

    public boolean isDaemonMode() {
        return daemonMode;
    }

    public void setDaemonMode(boolean daemonMode) {
        this.daemonMode = daemonMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String[] getRegions() {
        return regions;
    }

    public void setRegions(String[] regions) {
        this.regions = regions;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public TemporaryConfigData getTemporaryData() {
        return temporaryConfigData;
    }

    public void setTemporaryConfigData(TemporaryConfigData temporaryConfigData) {
        this.temporaryConfigData = temporaryConfigData;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
