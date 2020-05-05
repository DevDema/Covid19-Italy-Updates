import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;

public class DataGetTest {

    ApiHelper apiHelper = new ApiHelper();

    @Test
    public void getItalyData() {
        CovidItaData itaData = apiHelper.getItalyData();

        if(itaData != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data: \n" + itaData.toString());
        }

        assert itaData != null && itaData.getCountryCode() != null && !itaData.getCountryCode().isEmpty();
    }

    @Test
    public void getRegionsData() {
        List<CovidRegionData> regionsData = apiHelper.getRegionsData();

        if(regionsData != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data: \n" + regionsData.toString());
        }

        assert regionsData != null && !regionsData.isEmpty();
    }

    @Test
    public void computeVariationsOnSamples() {
        CovidItaData today = new CovidItaData();
        CovidItaData yesterday = new CovidItaData();

        today.setTotalCases(211938); //4 MAY 2020
        today.setDeaths(29079);
        today.setTotalRecovered(82879);
        today.setTotalPositive(99980);
        today.setQuarantined(81678);
        today.setIntensiveCare(1479);
        today.setHospitalized(18302);
        today.setTestedPeople(1479910);
        today.setTests(2191403);
        today.setVariationPositive(-199);

        yesterday.setTotalCases(210717); //3 MAY 2020
        yesterday.setDeaths(28884);
        yesterday.setTotalRecovered(81654);
        yesterday.setTotalPositive(100179);
        yesterday.setQuarantined(81436);
        yesterday.setIntensiveCare(1501);
        yesterday.setHospitalized(18743);
        yesterday.setTestedPeople(1456911);
        yesterday.setTests(2153772);

        CovidDataUtils.computeVariations(today, yesterday);

        assert today.getVariationTotalCases() == 1221;
        assert today.getVariationDeaths() == 195;
        assert today.getVariationRecovered() == 1225;
        assert today.getVariationPositive() == -199; //OBVIOUS, BUT ANYWAY...
        assert today.getVariationIntensiveCare() == -22;
        assert today.getVariationHospitalized() == -441;
        assert today.getVariationTestedPeople() == 22999;
        assert today.getVariationTests() == 37631;
        assert today.getVariationQuarantined() == 242;
    }
}
