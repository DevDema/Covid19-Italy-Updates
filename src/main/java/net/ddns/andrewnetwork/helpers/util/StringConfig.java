package net.ddns.andrewnetwork.helpers.util;

import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class StringConfig {

    public static final String EXCEPTION_INVALID_COUNTRY = "Enter a valid country type: {ITALY, ...}";
    public static final String EXCEPTION_MISSING_ARGUMENTS = "You need at least 1 argument.";
    public static final String EXCEPTION_MULTIPLE_LANGUAGES = "Only one language must be specified.";
    public static final String EXCEPTION_UNSUPPORTED_LANGUAGE = "The only language supported is ITA.";
    public static final String EXCEPTION_MISSING_ARGUMENTS_OPTION = "Missing arguments for option -";

    public static String buildFinalMessage(Date date, CovidItaData italyData, List<CovidRegionData> regionDataList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);

        StringBuilder finalSB = new StringBuilder("Aggiornamento situazione Covid-19 del " + dateString + ":");

        finalSB.append(italyData);

        for(CovidRegionData datum : regionDataList) {
            finalSB.append(datum);
        }

        finalSB.append("\n\nLink Utili:\n" +
                "<a href=\"https://datastudio.google.com/u/0/reporting/91350339-2c97-49b5-92b8-965996530f00\">Coronavirus Italia Google Data Studio</a>\n" +
                "<a href=\"https://www.worldometers.info/coronavirus/\">WorldOMeters: Dati dall'Italia e dal Mondo</a>");

        return finalSB.toString();
    }
}
