package net.ddns.andrewnetwork.helpers.async;

import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class AsyncCall {
    private static final ApiHelper apiHelper = new ApiHelper();

    public static Single<CovidItaData> getItalyData() {
        return Single.create(emitter -> {
            try {
                CovidItaData newData = apiHelper.getItalyData();

                newData = computeVariationsItalyData(newData);
                emitter.onSuccess(newData);
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    public static Observable<CovidItaData> getAllItalyData() {
        return Observable.create(emitter -> {
            try {
                List<CovidItaData> newData = apiHelper.getAllItalyData();
                newData = newData.stream().sorted(Comparator.comparing(CovidItaData::getDate)).collect(Collectors.toList());

                for (CovidItaData covidItaData : newData) {
                    covidItaData = computeVariationsItalyData(covidItaData);
                    emitter.onNext(covidItaData);
                }

                emitter.onCompleted();
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    public static Observable<CovidItaData> getAllItalyDataFrom(Date date) {
        return Observable.create(emitter -> {
            try {
                List<CovidItaData> newData = apiHelper.getAllItalyData();
                newData = newData.stream().filter(covidItaData -> covidItaData.getDate().compareTo(date) >= 0).sorted(Comparator.comparing(CovidItaData::getDate)).collect(Collectors.toList());

                for (CovidItaData covidItaData : newData) {
                    covidItaData = computeVariationsItalyData(covidItaData);
                    emitter.onNext(covidItaData);
                }

                emitter.onCompleted();
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    public static Observable<List<CovidRegionData>> getAllRegionsData(String[] regions) {
        return Observable.create(emitter -> {
            try {
                List<CovidRegionData> newData = apiHelper.getAllRegionsData();

                Map<Date, List<CovidRegionData>> regionsByDate = CovidDataUtils.groupRegionsByDate(newData);
                handleAllRegions(regions, emitter, regionsByDate);
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    public static Observable<List<CovidRegionData>> getAllRegionsDataFrom(String[] regions, Date date) {
        return Observable.create(emitter -> {
            try {
                List<CovidRegionData> newData = apiHelper.getAllRegionsData();

                Map<Date, List<CovidRegionData>> regionsByDate = CovidDataUtils.groupRegionsByDateFrom(newData, date);
                handleAllRegions(regions, emitter, regionsByDate);
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    private static void handleAllRegions(String[] regions, Subscriber<? super List<CovidRegionData>> emitter, Map<Date, List<CovidRegionData>> regionsByDate) {
        List<Date> keyDates = regionsByDate.keySet().stream().sorted(Date::compareTo).collect(Collectors.toList());

        for (Date keyDate : keyDates) {
            List<CovidRegionData> covidRegionDataList = regionsByDate.get(keyDate);
            covidRegionDataList = computeVariationsRegionsData(regions, covidRegionDataList);
            emitter.onNext(covidRegionDataList);
        }

        emitter.onCompleted();
    }

    public static Single<List<CovidRegionData>> getRegionsData(String[] regions) {
        if (regions == null || regions.length == 0) {
            throw new IllegalArgumentException("cannot pass empty regions data.");
        }
        return Single.create(emitter -> {
            try {
                List<CovidRegionData> data = apiHelper.getRegionsData();

                data = computeVariationsRegionsData(regions, data);
                emitter.onSuccess(data);
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    private static CovidItaData computeVariationsItalyData(CovidItaData newData) {
        if (newData != null) {
            CovidItaData savedData = SavedDataBuilder.getSavedData().getLastDay() != null ?
                    SavedDataBuilder.getSavedData().getLastDay().getItalyDataSaved() : null;

            if (savedData == null) {
                return newData;
            }

            savedData.setDate(DateUtil.setMidnight(savedData.getDate()));
            newData.setDate(DateUtil.setMidnight(newData.getDate()));

            //The newdata captured a Date equal to the last recorded date.
            //Therefore, data was edited. We shall retrieve yesterday and compute variations on that instead.
            if (DateUtil.isSameDay(newData.getDate(), savedData.getDate()) && !newData.equals(savedData)) {
                Calendar yesterdayCalendar = DateUtil.toCalendar(newData.getDate());
                yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
                Date yesterday = yesterdayCalendar.getTime();

                savedData = SavedDataBuilder.getSavedData().getDayBy(yesterday) != null ?
                        SavedDataBuilder.getSavedData().getDayBy(yesterday).getItalyDataSaved() : null;
            }

            CovidDataUtils.computeVariations(newData, savedData);

            return newData;
        } else {
            throw new IllegalArgumentException("Italy Data is null.");
        }
    }

    private static List<CovidRegionData> computeVariationsRegionsData(String[] regions, List<CovidRegionData> data) {
        List<CovidRegionData> newData = regions.length > 0 ? CovidDataUtils.getRegionByLabel(data, regions) : data;
        Collection<CovidRegionData> savedData = SavedDataBuilder.getSavedData().getLastDay() != null ?
                SavedDataBuilder.getSavedData().getLastDay().getRegionsDataSaved() : null;

        if (savedData == null || savedData.isEmpty()) {
            return newData;
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

            savedData = SavedDataBuilder.getSavedData().getDayBy(yesterday) != null ?
                    SavedDataBuilder.getSavedData().getDayBy(yesterday).getRegionsDataSaved() : null;
        }

        CovidDataUtils.computeVariationsList(newData, savedData);
        return newData;
    }
}
