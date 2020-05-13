package net.ddns.andrewnetwork.helpers.util;

import net.ddns.andrewnetwork.model.ConfigSavedData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        for(CovidRegionData data : newData) {
            CovidRegionData saved = CovidDataUtils.getRegionByLabel(savedData, data.getRegionLabel());

            computeVariations(data, saved);
        }
    }

    public static void computeVariations(CovidItaData data, CovidItaData saved) {
        data.setVariationDeaths(data.getDeaths() - saved.getDeaths());
        data.setVariationHospitalized(data.getHospitalized() - saved.getHospitalized());
        data.setVariationIntensiveCare(data.getIntensiveCare() - saved.getIntensiveCare());
        data.setVariationRecovered(data.getTotalRecovered() - saved.getTotalRecovered());
        data.setVariationTestedPeople(data.getTestedPeople() - saved.getTestedPeople());
        data.setVariationTests(data.getTests() - saved.getTests());
        data.setVariationTotalCases(data.getTotalCases() - saved.getTotalCases());
        data.setVariationQuarantined(data.getQuarantined() - saved.getQuarantined());
    }

    public static ConfigSavedData getByDate(Collection<ConfigSavedData> data, Date date) {
        for(ConfigSavedData saved : data) {
            if(saved.getDate().getTime() == date.getTime()) {
                return saved;
            }
        }

        return null;
    }

    public static ConfigSavedData getMostRecentDate(Collection<ConfigSavedData> lastDays) {
        return lastDays.stream()
                .filter(configSavedData -> configSavedData.getDate() != null)
                .max(Comparator.comparing(ConfigSavedData::getDate))
                .orElse(null);
    }
}
