package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

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
    public String toString() {
        return "\n\n<b>" +regionLabel.toUpperCase() + "</b>:\n" + '\'' +
                "date=" + date +
                ", countryCode='" + countryCode + '\'' +
                ", intensiveCare=" + intensiveCare +
                ", hospitalizedSymptoms=" + hospitalizedSymptoms +
                ", hospitalized=" + hospitalized +
                ", houseIsolation=" + houseIsolation +
                ", totalPositive=" + totalPositive +
                ", variationPositive=" + variationPositive + "" +
                ", newPositives=" + newPositives +
                ", totalRecovered=" + totalRecovered +
                ", deaths=" + deaths +
                ", totalCases=" + totalCases +
                ", tests=" + tests +
                ", testedPeople=" + testedPeople;
    }
}
