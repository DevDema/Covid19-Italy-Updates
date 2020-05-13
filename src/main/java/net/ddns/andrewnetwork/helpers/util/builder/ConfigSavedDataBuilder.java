package net.ddns.andrewnetwork.helpers.util.builder;

import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.model.ConfigSavedData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder.getConfigData;

public class ConfigSavedDataBuilder {

    private static final ConfigSavedDataBuilder configSavedDataBuilder = new ConfigSavedDataBuilder();
    private Collection<ConfigSavedData> configSavedDataList;
    private ConfigSavedData configSavedData;

    public ConfigSavedDataBuilder newData() {
        initData();
        this.configSavedData = new ConfigSavedData();

        return this;
    }

    private void initData() {
        Collection<ConfigSavedData> configSavedDataList1 = getConfigData().getLastDays();
        this.configSavedDataList = configSavedDataList1 != null ? configSavedDataList1 : new ArrayList<>();
    }

    public static ConfigSavedData getDate(Date date) {
        return getDate(getConfigData().getLastDays(), date);
    }

    public static ConfigSavedData getDate(Collection<ConfigSavedData> configSavedDataList, Date date) {
        return CovidDataUtils.getByDate(configSavedDataList, date);
    }

    public static ConfigSavedData getLastDate() {
        return CovidDataUtils.getMostRecentDate(getConfigData().getLastDays());
    }

    public ConfigSavedDataBuilder putDate(Date date) {
        if(configSavedData == null) {
            throw new IllegalStateException("putDate() called without calling newData() first.");
        }

        configSavedData.setDate(date);

        return this;
    }

    public ConfigSavedDataBuilder putTodayData(CovidItaData covidItaData, Collection<CovidRegionData> covidRegionDataList) {
        if(configSavedData == null) {
            throw new IllegalStateException("putTodayData() called without calling newData() first.");
        }

        configSavedData.setItalyDataSaved(covidItaData);
        configSavedData.setRegionsDataSaved(covidRegionDataList);

        return this;
    }

    public Collection<ConfigSavedData> build() {
        if(configSavedData == null) {
            throw new IllegalStateException("build() called without calling newData() first.");
        }

        configSavedDataList.add(configSavedData);

        return configSavedDataList;
    }

    public ConfigSavedDataBuilder getData(Date date) {
        initData();
        configSavedData = CovidDataUtils.getByDate(configSavedDataList, date);

        return this;
    }

    public ConfigSavedDataBuilder getLastData() {
        initData();
        configSavedData = CovidDataUtils.getMostRecentDate(configSavedDataList);

        return this;
    }

    public static ConfigSavedDataBuilder getInstance() {
        return configSavedDataBuilder;
    }
}
