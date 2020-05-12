package net.ddns.andrewnetwork.helpers.async;

import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.ConfigHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import rx.Single;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AsyncCall {
    private static final ApiHelper apiHelper = new ApiHelper();

    public static Single<CovidItaData> getItalyData() {
        return Single.create(emitter -> {
            CovidItaData newData = apiHelper.getItalyData();

            if(newData != null) {
                CovidItaData savedData = ConfigHelper.getConfigData().getItalyDataSaved();

                CovidDataUtils.computeVariations(newData, savedData);
                emitter.onSuccess(newData);
            } else {
                emitter.onError(new Exception("Covid Ita data is null."));
            }
        });
    }

    public static Single<Set<CovidRegionData>> getRegionsData(String[] regions) {
        return Single.create(emitter -> {
            List<CovidRegionData> data = apiHelper.getRegionsData();

            if(data != null) {
                Set<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(data, regions);
                Collection<CovidRegionData> savedData = ConfigHelper.getConfigData().getRegionsDataSaved();
                CovidDataUtils.computeVariationsList(newData, savedData);

                emitter.onSuccess(newData);
            } else {
                emitter.onError(new Exception("Covid data region is null."));
            }
        });
    }

}
