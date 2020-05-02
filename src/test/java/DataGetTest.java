import net.ddns.andrewnetwork.helpers.ApiHelper;
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
}
