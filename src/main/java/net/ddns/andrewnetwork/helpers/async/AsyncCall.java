package net.ddns.andrewnetwork.helpers.async;

import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.ConfigHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import rx.Single;

import java.util.List;

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

    public static Single<List<CovidRegionData>> getRegionsData(String[] regions) {
        return Single.create(emitter -> {
            List<CovidRegionData> data = apiHelper.getRegionsData();

            if(data != null) {
                List<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(data, regions);
                List<CovidRegionData> savedData = ConfigHelper.getConfigData().getRegionsDataSaved();

                CovidDataUtils.computeVariationsList(newData, savedData);
                emitter.onSuccess(newData);
            } else {
                emitter.onError(new Exception("Covid data region is null."));
            }
        });
    }

}
