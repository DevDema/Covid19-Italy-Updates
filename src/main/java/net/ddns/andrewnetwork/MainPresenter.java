package net.ddns.andrewnetwork;

import net.ddns.andrewnetwork.helpers.async.AsyncCall;
import net.ddns.andrewnetwork.helpers.util.Wrapper2;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import rx.Single;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MainPresenter {

    public void getData(String[] regions) {
        Single.zip(AsyncCall.getItalyData(),
                AsyncCall.getRegionsData(regions),
                Wrapper2::new).subscribe(dataWrapper -> {
                    CovidItaData covidItaData = dataWrapper.getObj1();
                    Set<CovidRegionData> covidRegionDataList = dataWrapper.getObj2();

                    MainEntry.onDataLoaded(covidItaData, covidRegionDataList);
                }
                , throwable -> Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("ERROR COULD NOT RETRIEVE DATA: " + throwable.getCause())
        );
    }
}
