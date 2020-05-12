package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        CovidRegionData that = (CovidRegionData) o;

        return regionCode == that.regionCode && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), regionCode, regionLabel);
    }
}
