package net.ddns.andrewnetwork.helpers.util;

import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.List;
import java.util.stream.Collectors;

public final class CovidDataUtils {

    public static CovidRegionData getRegionByCode(List<CovidRegionData> list, int code) {
        return list.stream().filter(covidRegionData -> covidRegionData.getRegionCode() == code).findAny().orElse(null);
    }

    public static CovidRegionData getRegionByLabel(List<CovidRegionData> list, String region) {
        return list.stream().filter(covidRegionData -> covidRegionData.getRegionLabel().equalsIgnoreCase(region)).findAny().orElse(null);
    }

    public static List<CovidRegionData> getRegionByLabel(List<CovidRegionData> list, String... regions) {
        return list.stream().filter(covidRegionData -> {
            boolean bool = false;
            for(String region : regions) {
                bool = covidRegionData.getRegionLabel().equalsIgnoreCase(region);

                if(bool) {
                    return true;
                }
            }

            return bool;
        }).collect(Collectors.toList());
    }
}
