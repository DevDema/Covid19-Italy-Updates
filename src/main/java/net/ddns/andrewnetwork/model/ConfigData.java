package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ConfigData {

    @SerializedName("last_seen_date")
    private Date date;
    @SerializedName("channel_id")
    private long channelID;
    @SerializedName("italy_data_saved")
    private CovidItaData italyDataSaved;
    @SerializedName("regions_data_saved")
    private List<CovidRegionData> regionsDataSaved;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getChannelID() {
        return channelID;
    }

    public CovidItaData getItalyDataSaved() {
        return italyDataSaved;
    }

    public void setItalyDataSaved(CovidItaData italyDataSaved) {
        this.italyDataSaved = italyDataSaved;
    }

    public List<CovidRegionData> getRegionsDataSaved() {
        return regionsDataSaved;
    }

    public void setRegionsDataSaved(List<CovidRegionData> regionsDataSaved) {
        this.regionsDataSaved = regionsDataSaved;
    }
}
