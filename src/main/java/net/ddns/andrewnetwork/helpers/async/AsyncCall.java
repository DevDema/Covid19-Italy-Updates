package net.ddns.andrewnetwork.helpers.async;

import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import rx.Single;

import java.util.*;

public final class AsyncCall {
    private static final ApiHelper apiHelper = new ApiHelper();

    public static Single<CovidItaData> getItalyData() {
        return Single.create(emitter -> {
            CovidItaData newData = apiHelper.getItalyData();

            if(newData != null) {
                CovidItaData savedData = SavedDataBuilder.getSavedData() != null && SavedDataBuilder.getSavedData().getLastDay() != null ? SavedDataBuilder.getSavedData().getLastDay().getItalyDataSaved() : null;

                if (savedData == null) {
                    emitter.onSuccess(newData);
                    return;
                }

                savedData.setDate(DateUtil.setMidnight(savedData.getDate()));
                newData.setDate(DateUtil.setMidnight(newData.getDate()));

                //The newdata captured a Date equal to the last recorded date.
                //Therefore, data was edited. We shall retrieve yesterday and compute variations on that instead.
                if (DateUtil.isSameDay(newData.getDate(), savedData.getDate()) && !newData.equals(savedData)) {
                    Calendar yesterdayCalendar = DateUtil.toCalendar(newData.getDate());
                    yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
                    Date yesterday = yesterdayCalendar.getTime();

                    savedData = SavedDataBuilder.getSavedData() != null && SavedDataBuilder.getSavedData().getDayBy(yesterday) != null ?
                            SavedDataBuilder.getSavedData().getDayBy(yesterday).getItalyDataSaved() : null;
                }

                CovidDataUtils.computeVariations(newData, savedData);
                emitter.onSuccess(newData);
            } else {
                emitter.onError(new Exception("Covid Ita data is null."));
            }
        });
    }

    public static Single<Set<CovidRegionData>> getRegionsData(String[] regions) {
        if (regions == null || regions.length == 0) {
            throw new IllegalArgumentException("cannot pass empty regions data.");
        }
        return Single.create(emitter -> {
            List<CovidRegionData> data = apiHelper.getRegionsData();


            if (data != null && !data.isEmpty()) {
                Set<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(data, regions);
                Collection<CovidRegionData> savedData = SavedDataBuilder.getSavedData() != null && SavedDataBuilder.getSavedData().getLastDay() != null ?
                        SavedDataBuilder.getSavedData().getLastDay().getRegionsDataSaved() : null;

                if (savedData == null || savedData.isEmpty()) {
                    emitter.onSuccess(newData);
                    return;
                }

                CovidRegionData anyNewRegion = newData.stream()
                        .filter(covidRegionData -> covidRegionData.getRegionLabel().equals(regions[0]))
                        .findFirst()
                        .orElse(null);

                CovidRegionData anySavedRegion = savedData.stream()
                        .filter(covidRegionData -> covidRegionData.getRegionLabel().equals(regions[0]))
                        .findFirst()
                        .orElse(null);

                //The newdata captured a Date equal to the last recorded date.
                //Therefore, data was edited. We shall retrieve yesterday and compute variations on that instead.
                //Ignore possible NullPointer. They can't occur.
                if (DateUtil.isSameDay(anyNewRegion.getDate(), anySavedRegion.getDate()) && !newData.equals(savedData)) {
                    Calendar yesterdayCalendar = DateUtil.toCalendar(anyNewRegion.getDate());
                    yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
                    Date yesterday = yesterdayCalendar.getTime();

                    savedData = SavedDataBuilder.getSavedData() != null && SavedDataBuilder.getSavedData().getDayBy(yesterday) != null ?
                            SavedDataBuilder.getSavedData().getDayBy(yesterday).getRegionsDataSaved() : null;
                }

                CovidDataUtils.computeVariationsList(newData, savedData);
                emitter.onSuccess(newData);
            } else {
                emitter.onError(new Exception("Covid data region is null or empty."));
            }
        });
    }

}
