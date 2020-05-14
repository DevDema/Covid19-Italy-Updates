import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigSavedDataBuilder;
import net.ddns.andrewnetwork.model.ConfigData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class CachingTest {

    ApiHelper apiHelper = new ApiHelper();

    @BeforeEach
    public void setupEach() {
        ConfigDataBuilder.clear();
    }

    @AfterAll
    public static void setupAfter() {
        ConfigDataBuilder.clear();
    }

    @Test
    public void cacheCovidData() {
        CovidItaData itaData = apiHelper.getItalyData();
        List<CovidRegionData> regionsData = apiHelper.getRegionsData();

        assert regionsData != null;

        Set<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(regionsData, "Lombardia", "Puglia");

        ConfigDataBuilder.getInstance()
                .getData()
                .putDays(ConfigSavedDataBuilder.getInstance()
                        .newData()
                        .putTodayData(itaData, newData)
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
