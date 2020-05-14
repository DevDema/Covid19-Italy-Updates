package net.ddns.andrewnetwork.helpers.util.builder;

import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.ConfigSavedData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.*;
import java.util.stream.Collectors;

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

    public ConfigSavedDataBuilder putTodayData(CovidItaData covidItaData, Collection<CovidRegionData> covidRegionDataList) {
        if(configSavedData == null) {
            throw new IllegalStateException("putTodayData() called without calling newData() first.");
        }

        configSavedData.setItalyDataSaved(covidItaData);
        configSavedData.setRegionsDataSaved(covidRegionDataList);
        configSavedData.setDate(DateUtil.max(covidItaData.getDate(),
                covidRegionDataList.stream().map(CovidItaData::getDate).collect(Collectors.toSet())));

        return this;
    }

    public Collection<ConfigSavedData> build() {
        if(configSavedData == null) {
            throw new IllegalStateException("build() called without calling newData() first.");
        }

        if(configSavedData.getDate() == null) {
            throw new IllegalArgumentException("build() called on null last_seen_date");
        }

        configSavedDataList.removeIf(configSavedData1 -> DateUtil.isSameDay(configSavedData1.getDate(), configSavedData.getDate()));
        configSavedDataList.add(configSavedData);

        return configSavedDataList;
    }

    public ConfigSavedDataBuilder getData(Date date) {
        initData();
        this.configSavedData = configSavedDataList != null && !configSavedDataList.isEmpty() ?
                CovidDataUtils.getByDate(configSavedDataList, date) : new ConfigSavedData();

        return this;
    }

    public ConfigSavedDataBuilder getLastData() {
        initData();
        ConfigSavedData configSavedData = CovidDataUtils.getMostRecentDate(configSavedDataList);
        this.configSavedData = configSavedData != null ? configSavedData : new ConfigSavedData();

        return this;
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

    public static ConfigSavedDataBuilder getInstance() {
        return configSavedDataBuilder;
    }
}
