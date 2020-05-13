import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigSavedDataBuilder;
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

        ConfigDataBuilder.getInstance()
                .getData()
                .putDays(ConfigSavedDataBuilder.getInstance()
                        .newData()
                        .putDate(itaData.getDate())
                        .putTodayData(itaData, regionsData)
                        .build())
                .commit();

        assert ConfigDataBuilder.getConfigData() != null;
        assert ConfigDataBuilder.getConfigData().getLastDay().getDate() != null;
        assert ConfigDataBuilder.getConfigData().getLastDay().getItalyDataSaved() != null;
        assert ConfigDataBuilder.getConfigData().getLastDay().getRegionsDataSaved() != null &&
                !ConfigDataBuilder.getConfigData().getLastDay().getRegionsDataSaved().isEmpty();
    }

    @Test
    public void cacheMessageId() {
        long messageId = 4024372L;

        ConfigDataBuilder.getInstance()
                .getData()
                .putMessageId(messageId)
                .commit();

        assert ConfigDataBuilder.getConfigData().getMessageID() == messageId;
    }
}
