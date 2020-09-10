package net.ddns.andrewnetwork.helpers.util;

import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import net.ddns.andrewnetwork.model.SavedDataDay;

import java.util.*;
import java.util.stream.Collectors;

public final class CovidDataUtils {

    public static CovidRegionData getRegionByCode(List<CovidRegionData> list, int code) {
        return list.stream().filter(covidRegionData -> covidRegionData.getRegionCode() == code).findAny().orElse(null);
    }

    public static CovidRegionData getRegionByLabel(Collection<CovidRegionData> list, String region) {
        return list.stream().filter(covidRegionData -> covidRegionData.getRegionLabel().equalsIgnoreCase(region)).findAny().orElse(null);
    }

    public static Set<CovidRegionData> getRegionByLabel(Collection<CovidRegionData> list, String... regions) {
        return list.stream().filter(covidRegionData -> {
            boolean bool = false;
            for(String region : regions) {
                bool = covidRegionData.getRegionLabel().equalsIgnoreCase(region);

                if(bool) {
                    return true;
                }
            }

            return bool;
        }).collect(Collectors.toSet());
    }

    public static void computeVariationsList(Collection<CovidRegionData> newData, Collection<CovidRegionData> savedData) {
        if(savedData == null || savedData.isEmpty()) {
            return;
        }

        for(CovidRegionData data : newData) {
            CovidRegionData saved = CovidDataUtils.getRegionByLabel(savedData, data.getRegionLabel());

            computeVariations(data, saved);
        }
    }

    public static void computeVariations(CovidItaData data, CovidItaData saved) {
        if(saved == null) {
            return;
        }

        data.setVariationDeaths(data.getDeaths() - saved.getDeaths());
        data.setVariationHospitalized(data.getHospitalized() - saved.getHospitalized());
        data.setVariationIntensiveCare(data.getIntensiveCare() - saved.getIntensiveCare());
        data.setVariationRecovered(data.getTotalRecovered() - saved.getTotalRecovered());
        data.setVariationTestedPeople(data.getTestedPeople() - saved.getTestedPeople());
        data.setVariationTests(data.getTests() - saved.getTests());
        data.setVariationTotalCases(data.getTotalCases() - saved.getTotalCases());
        data.setVariationQuarantined(data.getQuarantined() - saved.getQuarantined());
    }

    public static SavedDataDay getByDate(Collection<SavedDataDay> data, Date date) {
        for (SavedDataDay saved : data) {
            if (saved.getDate().getTime() == date.getTime()) {
                return saved;
            }
        }

        return null;
    }

    public static SavedDataDay getMostRecentDate(Collection<SavedDataDay> lastDays) {
        return lastDays.stream()
                .filter(configSavedData -> configSavedData.getDate() != null)
                .max(Comparator.comparing(SavedDataDay::getDate))
                .orElse(null);
    }

    public static boolean areImpossibleDataNegative(CovidItaData covidItaData) {
        return covidItaData.getVariationTests() < 0 || covidItaData.getVariationTestedPeople() < 0 || covidItaData.getVariationTotalCases() < 0 || covidItaData.getVariationDeaths() < 0;
    }
}
