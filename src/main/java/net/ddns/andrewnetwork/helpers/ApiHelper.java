package net.ddns.andrewnetwork.helpers;

import com.google.gson.reflect.TypeToken;
import net.ddns.andrewnetwork.helpers.util.JsonUtil;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Logger.GLOBAL_LOGGER_NAME;

public final class ApiHelper {

    private static final String ITALY_DATA_URL = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale-latest.json";
    private static final String REGIONS_DATA_URL = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni-latest.json";

    private static String readFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        is.close();

        return sb.toString();

    }

    public CovidItaData getItalyData() {

        try {
            String data = readFromUrl(ITALY_DATA_URL);

            List<CovidItaData> list =  JsonUtil.getGson().fromJson(data, new TypeToken<List<CovidItaData>>(){}.getType());

            return list.get(0);
        } catch (IOException e) {
            Logger.getLogger(GLOBAL_LOGGER_NAME).warning("Malformed Italy data.");

            return null;
        }
    }

    public List<CovidRegionData> getRegionsData() {
        try {
            String data = readFromUrl(REGIONS_DATA_URL);

            return JsonUtil.getGson().fromJson(data, new TypeToken<List<CovidRegionData>>(){}.getType());
        } catch (IOException e) {
            Logger.getLogger(GLOBAL_LOGGER_NAME).warning("Malformed Italy Regions data.");

            return null;
        }
    }
}
