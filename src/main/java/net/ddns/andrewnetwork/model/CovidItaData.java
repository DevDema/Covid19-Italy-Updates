package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;
import net.ddns.andrewnetwork.helpers.util.StringConfig;

import java.io.Serializable;
import java.util.Date;

public class CovidItaData implements Serializable {

    @SerializedName("data")
    protected Date date;
    @SerializedName("stato")
    protected String countryCode;
    @SerializedName("terapia_intensiva")
    protected int intensiveCare;
    @SerializedName("ricoverati_con_sintomi")
    protected int hospitalizedSymptoms;
    @SerializedName("totale_ospedalizzati")
    protected int hospitalized;
    @SerializedName("isolamento_domiciliare")
    protected  int houseIsolation;
    @SerializedName("totale_positivi")
    protected int totalPositive;
    @SerializedName("nuovi_positivi")
    protected int newPositives;
    @SerializedName("dimessi_guariti")
    protected int totalRecovered;
    @SerializedName("deceduti")
    protected  int deaths;
    @SerializedName("totale_casi")
    protected int totalCases;
    @SerializedName("tamponi")
    protected int tests;
    @SerializedName("casi_testati")
    protected int testedPeople;

    @SerializedName("variazione_totale_positivi")
    protected int variationPositive;
    protected transient int variationDeaths;
    protected transient int variationRecovered;
    protected transient int variationTests;
    protected transient int variationTestedPeople;
    protected transient int variationHospitalized;
    protected transient int variationIntensiveCare;
    protected transient int variationTotalCases;

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

    public int getVariationDeaths() {
        return variationDeaths;
    }

    public int getVariationRecovered() {
        return variationRecovered;
    }

    public int getVariationHospitalized() {
        return variationHospitalized;
    }

    public int getVariationIntensiveCare() {
        return variationIntensiveCare;
    }

    public int getVariationTestedPeople() {
        return variationTestedPeople;
    }

    public int getVariationTotalCases() {
        return variationTotalCases;
    }

    public int getVariationTests() {
        return variationTests;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setIntensiveCare(int intensiveCare) {
        this.intensiveCare = intensiveCare;
    }

    public void setHospitalizedSymptoms(int hospitalizedSymptoms) {
        this.hospitalizedSymptoms = hospitalizedSymptoms;
    }

    public void setHospitalized(int hospitalized) {
        this.hospitalized = hospitalized;
    }

    public void setHouseIsolation(int houseIsolation) {
        this.houseIsolation = houseIsolation;
    }

    public void setTotalPositive(int totalPositive) {
        this.totalPositive = totalPositive;
    }

    public void setNewPositives(int newPositives) {
        this.newPositives = newPositives;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public void setTestedPeople(int testedPeople) {
        this.testedPeople = testedPeople;
    }

    public void setVariationDeaths(int variationDeaths) {
        this.variationDeaths = variationDeaths;
    }

    public void setVariationHospitalized(int variationHospitalized) {
        this.variationHospitalized = variationHospitalized;
    }

    public void setVariationIntensiveCare(int variationIntensiveCare) {
        this.variationIntensiveCare = variationIntensiveCare;
    }

    public void setVariationPositive(int variationPositive) {
        this.variationPositive = variationPositive;
    }

    public void setVariationTotalCases(int variationTotalPositive) {
        this.variationTotalCases = variationTotalPositive;
    }

    public void setVariationRecovered(int variationRecovered) {
        this.variationRecovered = variationRecovered;
    }

    public void setVariationTestedPeople(int variationTestedPeople) {
        this.variationTestedPeople = variationTestedPeople;
    }

    public void setVariationTests(int variationTests) {
        this.variationTests = variationTests;
    }

    protected String getTitleString() {
        return "ITALIA";
    }

    @Override
    public String toString() {
        return "\n\n<b>[" + getTitleString() + "]</b>:\n" +
                "<b>" + StringConfig.formatNumber(variationPositive) + "</b> <b>Variazione casi positivi</b>\n" +
                "<b>"+ StringConfig.formatNumber(variationDeaths) + "</b> <b>Variazione Morti</b>\n" +
                "<b>"+ StringConfig.formatNumber(variationRecovered) + "</b> <b>Variazione Guariti</b>\n" +
                "<b>"+ StringConfig.formatNumber(variationHospitalized) + "</b> <b>Variazione Ospedalizzati</b>\n" +
                "<b>"+ StringConfig.formatNumber(variationIntensiveCare) + "</b> <b>Variazione Terapia Intensiva</b>\n" +
                "<b>"+ StringConfig.formatNumber(variationTests) + "</b> <b>Variazione Tamponi</b>\n" +
                "<b>"+ StringConfig.formatNumber(variationTestedPeople) + "</b> <b>Variazione Persone testate</b>\n\n" +
                "<b>" + StringConfig.formatNumber(totalCases) +"</b> <b>Casi totali da inizio pandemia</b>\n" +
                "<b>" + StringConfig.formatNumber(totalPositive) + "</b> <b>Totale positivi</b>\n" +
                "<b>" + StringConfig.formatNumber(totalRecovered) + "</b> <b>Totale guariti</b>\n" +
                "<b>" + StringConfig.formatNumber(deaths) + "</b> <b>Totale morti</b>\n" +
                "<b>" + StringConfig.formatNumber(newPositives) + "</b> <b>Nuovi casi</b>\n" +
                "<b>" + StringConfig.formatNumber(houseIsolation) + "</b> in <b>Quarantena</b>\n" +
                "<b>" + StringConfig.formatNumber(hospitalized) + "</b> in <b>Ospedale</b>\n" +
                "<b>" + StringConfig.formatNumber(hospitalizedSymptoms) + "</b> in <b>Ospedale con sintomi</b>\n" +
                "<b>" + StringConfig.formatNumber(intensiveCare) + "</b> in <b>Terapia Intensiva</b>\n" +
                "<b>" + StringConfig.formatNumber(tests) + "</b> <b>Tamponi eseguiti</b>\n" +
                "<b>" + StringConfig.formatNumber(testedPeople) + "</b> <b>Persone testate</b>\n";
    }
}
