import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.ConfigHelper;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CachingTest {

    ApiHelper apiHelper = new ApiHelper();

    @Test
    public void cacheCovidData() {
        CovidItaData itaData = apiHelper.getItalyData();
        List<CovidRegionData> regionsData = apiHelper.getRegionsData();

        ConfigHelper.getInstance()
                .getData()
                .putDate(itaData.getDate())
                .putTodayData(itaData, regionsData)
                .commit();

        assert ConfigHelper.getConfigData() != null;
        assert ConfigHelper.getConfigData().getDate() != null;
        assert ConfigHelper.getConfigData().getItalyDataSaved() != null;
        assert ConfigHelper.getConfigData().getRegionsDataSaved() != null && !ConfigHelper.getConfigData().getRegionsDataSaved().isEmpty();

    }

    @Test
    public void cacheMessageId() {
        long messageId = 4024372L;

        ConfigHelper.getInstance()
                .getData()
                .putMessageId(messageId)
                .commit();

        assert ConfigHelper.getConfigData().getMessageID() == messageId;
    }
}
