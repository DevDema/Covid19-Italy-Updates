package net.ddns.andrewnetwork;

import net.ddns.andrewnetwork.helpers.async.AsyncCall;
import net.ddns.andrewnetwork.helpers.util.Wrapper2;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import rx.Observable;
import rx.Single;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainPresenter {

    private static void handleAllData(Wrapper2<CovidItaData, List<CovidRegionData>> dataWrapper) {
        CovidItaData covidItaData = dataWrapper.getObj1();
        List<CovidRegionData> covidRegionDataList = dataWrapper.getObj2();

        Set<Date> datesRegions = covidRegionDataList.stream().map(CovidItaData::getDate).collect(Collectors.toSet());

        if (!datesRegions.stream().allMatch(dateRegion -> dateRegion.equals(covidItaData.getDate()))) {
            throw new IllegalArgumentException("Dates are not equals. You can't put together CovidItaData and CovidRegionData with different days.");
        }

        MainEntry.onDataLoaded(covidItaData, covidRegionDataList);
    }

    public void getLatestDataBy(String[] regions) {
        Single.zip(AsyncCall.getItalyData(),
                AsyncCall.getRegionsData(regions),
                Wrapper2::new).subscribe(dataWrapper -> {
                    CovidItaData covidItaData = dataWrapper.getObj1();
                    List<CovidRegionData> covidRegionDataList = dataWrapper.getObj2();

                    MainEntry.onDataLoaded(covidItaData, covidRegionDataList);
                }
                , throwable -> Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Error retrieving data: " + throwable.getCause())
        );
    }

    public void getAllDataBy(String[] regions) {
        Observable.zip(AsyncCall.getAllItalyData(),
                AsyncCall.getAllRegionsData(regions),
                Wrapper2::new).subscribe(MainPresenter::handleAllData
                , throwable -> Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Error retrieving data: " + throwable.getCause())
        );
    }

    public void getAllDataByFrom(String[] regions, Date date) {
        Observable.zip(AsyncCall.getAllItalyDataFrom(date),
                AsyncCall.getAllRegionsDataFrom(regions, date),
                Wrapper2::new).subscribe(MainPresenter::handleAllData
                , throwable -> Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Error retrieving data: " + throwable.getCause())
        );
    }
}
