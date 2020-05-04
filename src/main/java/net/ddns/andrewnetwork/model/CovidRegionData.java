package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;
import net.ddns.andrewnetwork.helpers.util.StringConfig;

public class CovidRegionData extends CovidItaData {

    @SerializedName("codice_regione")
    int regionCode;
    @SerializedName("denominazione_regione")
    String regionLabel;

    public int getRegionCode() {
        return regionCode;
    }

    public String getRegionLabel() {
        return regionLabel;
    }

    @Override
    protected String getTitleString() {
        return regionLabel.toUpperCase();
    }
}
