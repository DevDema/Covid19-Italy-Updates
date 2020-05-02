package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class CovidItaData implements Serializable {

    @SerializedName("data")
    Date date;
    @SerializedName("stato")
    String countryCode;

    @SerializedName("terapia_intensiva")
    int intensiveCare;
    @SerializedName("ricoverati_con_sintomi")
    int hospitalizedSymptoms;
    @SerializedName("totale_ospedalizzati")
    int hospitalized;
    @SerializedName("isolamento_domiciliare")
    int houseIsolation;
    @SerializedName("totale_positivi")
    int totalPositive;
    @SerializedName("variazione_totale_positivi")
    int variationPositive;
    @SerializedName("nuovi_positivi")
    int newPositives;
    @SerializedName("dimessi_guariti")
    int totalRecovered;
    @SerializedName("deceduti")
    int deaths;
    @SerializedName("totale_casi")
    int totalCases;
    @SerializedName("tamponi")
    int tests;
    @SerializedName("casi_testati")
    int testedPeople;

    public Date getDate() {
        return date;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getIntensiveCare() {
        return intensiveCare;
    }

    public int getHospitalizedSymptoms() {
        return hospitalizedSymptoms;
    }

    public int getHospitalized() {
        return hospitalized;
    }

    public int getHouseIsolation() {
        return houseIsolation;
    }

    public int getTotalPositive() {
        return totalPositive;
    }

    public int getVariationPositive() {
        return variationPositive;
    }

    public int getNewPositives() {
        return newPositives;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getTests() {
        return tests;
    }

    public int getTestedPeople() {
        return testedPeople;
    }

    @Override
    public String toString() {
        return "\n\n<b>ITALIA</b>:\n" +
                "date=" + date +
                ", countryCode='" + countryCode + '\'' +
                ", intensiveCare=" + intensiveCare +
                ", hospitalizedSymptoms=" + hospitalizedSymptoms +
                ", hospitalized=" + hospitalized +
                ", houseIsolation=" + houseIsolation +
                ", totalPositive=" + totalPositive +
                ", variationPositive=" + variationPositive +
                ", newPositives=" + newPositives +
                ", totalRecovered=" + totalRecovered +
                ", deaths=" + deaths +
                ", totalCases=" + totalCases +
                ", tests=" + tests +
                ", testedPeople=" + testedPeople;
    }
}
