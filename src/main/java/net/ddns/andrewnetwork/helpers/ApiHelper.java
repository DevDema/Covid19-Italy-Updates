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

public final class ApiHelper {

    private static final String ITALY_DATA_URL = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale-latest.json";
    private static final String ITALY_DATA_ALL_URL = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json";
    private static final String REGIONS_DATA_URL = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni-latest.json";
    private static final String REGIONS_DATA_ALL_URL = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni.json";

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

    private List<CovidItaData> getItaDataFrom(String italyDataUrl) throws IOException {
        String data = readFromUrl(italyDataUrl);

        return JsonUtil.getGson().fromJson(data, new TypeToken<List<CovidItaData>>() {
        }.getType());
    }

    private List<CovidRegionData> getRegionsDataFrom(String regionsDataUrl) throws IOException {
        String data = readFromUrl(regionsDataUrl);

        return JsonUtil.getGson().fromJson(data, new TypeToken<List<CovidRegionData>>() {
        }.getType());
    }

    public CovidItaData getItalyData() throws IOException {

        return getItaDataFrom(ITALY_DATA_URL).get(0);
    }

    public List<CovidItaData> getAllItalyData() throws IOException {

        return getItaDataFrom(ITALY_DATA_ALL_URL);
    }

    public List<CovidRegionData> getRegionsData() throws IOException {
        return getRegionsDataFrom(REGIONS_DATA_URL);
    }

    public List<CovidRegionData> getAllRegionsData() throws IOException {
        return getRegionsDataFrom(REGIONS_DATA_ALL_URL);
    }
}
