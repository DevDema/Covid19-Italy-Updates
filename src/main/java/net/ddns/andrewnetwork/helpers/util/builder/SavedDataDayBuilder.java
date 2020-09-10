package net.ddns.andrewnetwork.helpers.util.builder;

import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import net.ddns.andrewnetwork.model.SavedDataDay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class SavedDataDayBuilder {

    private static final SavedDataDayBuilder SAVED_DATA_DAY_BUILDER = new SavedDataDayBuilder();
    private Collection<SavedDataDay> savedDataDayList;
    private SavedDataDay savedDataDay;

    public static SavedDataDay getDate(Date date) {
        return getDate(SavedDataBuilder.getSavedData().getLastDays(), date);
    }

    public static SavedDataDay getDate(Collection<SavedDataDay> savedDataDayList, Date date) {
        return CovidDataUtils.getByDate(savedDataDayList, date);
    }

    public static SavedDataDay getLastDate() {
        return CovidDataUtils.getMostRecentDate(SavedDataBuilder.getSavedData().getLastDays());
    }

    public static SavedDataDayBuilder getInstance() {
        return SAVED_DATA_DAY_BUILDER;
    }

    public SavedDataDayBuilder newData() {
        initData();
        this.savedDataDay = new SavedDataDay();

        return this;
    }

    private void initData() {
        Collection<SavedDataDay> savedDataDayList1 = SavedDataBuilder.getSavedData().getLastDays();
        this.savedDataDayList = savedDataDayList1 != null ? savedDataDayList1 : new ArrayList<>();
    }

    public SavedDataDayBuilder putTodayData(CovidItaData covidItaData, Collection<CovidRegionData> covidRegionDataList) {
        if (savedDataDay == null) {
            throw new IllegalStateException("putTodayData() called without calling newData() first.");
        }

        savedDataDay.setItalyDataSaved(covidItaData);
        savedDataDay.setRegionsDataSaved(covidRegionDataList);
        savedDataDay.setDate(DateUtil.max(covidItaData.getDate(),
                covidRegionDataList.stream().map(CovidItaData::getDate).collect(Collectors.toSet())));

        return this;
    }

    public Collection<SavedDataDay> build() {
        if (savedDataDay == null) {
            throw new IllegalStateException("build() called without calling newData() first.");
        }

        if (savedDataDay.getDate() == null) {
            throw new IllegalArgumentException("build() called on null last_seen_date");
        }

        savedDataDayList.removeIf(configSavedData1 -> DateUtil.isSameDay(configSavedData1.getDate(), savedDataDay.getDate()));
        savedDataDayList.add(savedDataDay);

        return savedDataDayList;
    }

    public SavedDataDayBuilder getData(Date date) {
        initData();
        this.savedDataDay = savedDataDayList != null && !savedDataDayList.isEmpty() ?
                CovidDataUtils.getByDate(savedDataDayList, date) : new SavedDataDay();

        return this;
    }

    public SavedDataDayBuilder getLastData() {
        initData();
        SavedDataDay savedDataDay = CovidDataUtils.getMostRecentDate(savedDataDayList);
        this.savedDataDay = savedDataDay != null ? savedDataDay : new SavedDataDay();

        return this;
    }
}
