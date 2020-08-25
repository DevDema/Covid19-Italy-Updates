package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CovidItaData implements Serializable, Cloneable {

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
    protected int quarantined;
    @SerializedName("totale_positivi")
    protected int totalPositive;
    @SerializedName("nuovi_positivi")
    protected int newPositives;
    @SerializedName("dimessi_guariti")
    protected int totalRecovered;
    @SerializedName("deceduti")
    protected int deaths;
    @SerializedName("totale_casi")
    protected int totalCases;
    @SerializedName("tamponi")
    protected int tests;
    @SerializedName("casi_testati")
    protected int testedPeople;

    @SerializedName("variazione_totale_positivi")
    protected int variationPositive;
    protected transient int variationQuarantined;
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

    public int getQuarantined() {
        return quarantined;
    }

    public int getTotalPositive() {
        return totalPositive;
    }

    public int getVariationPositive() {
        return variationPositive;
    }

    public int getVariationQuarantined() {
        return variationQuarantined;
    }

    public void setVariationQuarantined(int variationQuarantined) {
        this.variationQuarantined = variationQuarantined;
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

    public void setQuarantined(int quarantined) {
        this.quarantined = quarantined;
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
        return "ITALIA " + StringConfig.getItalyEmoji();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CovidItaData that = (CovidItaData) o;

        return intensiveCare == that.intensiveCare &&
                hospitalizedSymptoms == that.hospitalizedSymptoms &&
                hospitalized == that.hospitalized &&
                quarantined == that.quarantined &&
                totalPositive == that.totalPositive &&
                newPositives == that.newPositives &&
                totalRecovered == that.totalRecovered &&
                deaths == that.deaths &&
                totalCases == that.totalCases &&
                tests == that.tests &&
                testedPeople == that.testedPeople &&
                DateUtil.isSameDay(date, that.getDate()) &&
                Objects.equals(countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, countryCode, intensiveCare, hospitalizedSymptoms, hospitalized, quarantined, totalPositive, newPositives, totalRecovered, deaths, totalCases, tests, testedPeople);
    }

    @Override
    public String toString() {
        return "\n\n<b>[" + getTitleString() + "]</b>:\n" +
                "<b>" + StringConfig.formatVariation(newPositives) + "</b> " + (Math.abs(newPositives) != 1 ? "<b>Nuovi Casi</b>\n" : "<b>Nuovo Caso</b>\n") +
                "<b>" + StringConfig.formatVariation(variationPositive) + "</b> " + (Math.abs(variationPositive) != 1 ? "<b>Casi Positivi</b>\n" : "<b>Caso Positivo</b>\n") +
                (variationDeaths != 0 ? "<b>" + StringConfig.formatVariation(variationDeaths) + "</b> " + (Math.abs(variationDeaths) > 1 ? "<b>Morti</b>\n" : "<b>Morto</b>\n") : "") +
                (variationRecovered != 0 ? "<b>" + StringConfig.formatVariation(variationRecovered) + "</b> " + (Math.abs(variationRecovered) > 1 ? "<b>Guariti</b>\n" : "<b>Guarito</b>\n") : "") +
                (variationHospitalized != 0 ? "<b>" + StringConfig.formatVariation(variationHospitalized) + "</b> " + (Math.abs(variationHospitalized) > 1 ? "<b>Ospedalizzati</b>\n" : "<b>Ospedalizzato</b>\n") : "") +
                (variationIntensiveCare != 0 ? "<b>" + StringConfig.formatVariation(variationIntensiveCare) + "</b> in <b>Terapia Intensiva</b>\n" : "") +
                (variationQuarantined != 0 ? "<b>" + StringConfig.formatVariation(variationQuarantined) + "</b> in <b>Quarantena</b>\n" : "") +
                (variationTests != 0 ? "<b>" + StringConfig.formatVariation(variationTests) + "</b> " + (Math.abs(variationTests) > 1 ? "<b>Tamponi</b>\n" : "<b>Tampone</b>\n") : "") +
                (variationTestedPeople != 0 ? "<b>" + StringConfig.formatVariation(variationTestedPeople) + "</b> " + (Math.abs(variationTestedPeople) > 1 ? "<b>Persone Testate</b>\n" : "<b>Persona Testata</b>\n") : "") +
                "\n<b>" + StringConfig.formatNumber(totalCases) + "</b> Casi totali da inizio pandemia\n" +
                "<b>" + StringConfig.formatNumber(totalPositive) + "</b> Totale positivi\n" +
                "<b>" + StringConfig.formatNumber(totalRecovered) + "</b> Totale guariti\n" +
                "<b>" + StringConfig.formatNumber(deaths) + "</b> Totale morti\n" +
                "<b>" + StringConfig.formatNumber(quarantined) + "</b> in Quarantena\n" +
                "<b>" + StringConfig.formatNumber(hospitalized) + "</b> in Ospedale\n" +
                "<b>" + StringConfig.formatNumber(hospitalizedSymptoms) + "</b> in Ospedale con sintomi\n" +
                "<b>" + StringConfig.formatNumber(intensiveCare) + "</b> in Terapia Intensiva\n" +
                "<b>" + StringConfig.formatNumber(tests) + "</b> Tamponi eseguiti\n" +
                "<b>" + StringConfig.formatNumber(testedPeople) + "</b> Persone testate\n";
    }

    @Override
    public CovidItaData clone() throws CloneNotSupportedException {
        return (CovidItaData) super.clone();
    }
}
