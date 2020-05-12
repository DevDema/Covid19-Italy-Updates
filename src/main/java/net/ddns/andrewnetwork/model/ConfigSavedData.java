package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Date;

public class ConfigSavedData {

    @SerializedName("last_seen_date")
    private Date date;
    @SerializedName("italy_data_saved")
    private CovidItaData italyDataSaved;
    @SerializedName("regions_data_saved")
    private Collection<CovidRegionData> regionsDataSaved;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CovidItaData getItalyDataSaved() {
        return italyDataSaved;
    }

    public void setItalyDataSaved(CovidItaData italyDataSaved) {
        this.italyDataSaved = italyDataSaved;
    }

    public Collection<CovidRegionData> getRegionsDataSaved() {
        return regionsDataSaved;
    }

    public void setRegionsDataSaved(Collection<CovidRegionData> regionsDataSaved) {
        this.regionsDataSaved = regionsDataSaved;
    }
}
