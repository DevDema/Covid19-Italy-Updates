import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataDayBuilder;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class CachingTest {

    private static final long channelId = -1001446903259L;
    private final ApiHelper apiHelper = new ApiHelper();

    @BeforeAll
    public static void setup() {
        SavedDataBuilder.setSavedDataPath("saved-data-test.json");
        ConfigDataBuilder.setConfigPath("config-test.json");

        ConfigDataBuilder.getInstance()
                .putChannelId(channelId)
                .commit();
    }

    @AfterAll
    public static void setupAfter() {
        SavedDataBuilder.clear();
    }

    @BeforeEach
    public void setupEach() {
        SavedDataBuilder.clear();
    }

    @Test
    public void cacheCovidData() {
        CovidItaData itaData = apiHelper.getItalyData();
        List<CovidRegionData> regionsData = apiHelper.getRegionsData();

        assert regionsData != null;

        Set<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(regionsData, "Lombardia", "Puglia");

        SavedDataBuilder.getInstance()
                .getData()
                .putDays(SavedDataDayBuilder.getInstance()
                        .newData()
                        .putTodayData(itaData, newData)
                        .build())
                .commit();

        assert SavedDataBuilder.getSavedData() != null;
        assert SavedDataBuilder.getSavedData().getLastDay().getDate() != null;
        assert SavedDataBuilder.getSavedData().getLastDay().getItalyDataSaved() != null;
        assert SavedDataBuilder.getSavedData().getLastDay().getRegionsDataSaved() != null &&
                !SavedDataBuilder.getSavedData().getLastDay().getRegionsDataSaved().isEmpty();
    }

    @Test
    public void cacheMessageId() {
        long messageId = 4024372L;

        ConfigDataBuilder.getInstance()
                .getData()
                .putMessageId(messageId)
                .commit();

        assert ConfigDataBuilder.getMessageId() == messageId;
    }
}
