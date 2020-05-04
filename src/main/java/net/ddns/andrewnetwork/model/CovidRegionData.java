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
    public String toString() {
        return "\n\n<b>" +regionLabel.toUpperCase() + "</b>:\n" +
                "<b>" + StringConfig.formatNumber(variationPositive) + "</b><b> Variazione di casi positivi</b>\n\n" +
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
