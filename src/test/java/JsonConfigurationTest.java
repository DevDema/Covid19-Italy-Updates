import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class JsonConfigurationTest {

    private static final long channelId = -1001446903259L;

    @BeforeAll
    public static void setup() {
        ConfigDataBuilder.setConfigPath("config-test.json");
    }

    @BeforeEach
    public void setupEach() {
        ConfigDataBuilder.clear();

        ConfigDataBuilder.getInstance()
                .getData()
                .putChannelId(channelId)
                .commit();
    }

    @Test
    public void isDaemonMode() {
        ConfigDataBuilder.getInstance()
                .getData()
                .putDaemonMode(true)
                .commit();

        assert ConfigDataBuilder.getConfigData().isDaemonMode();
    }

    @Test
    public void isDebugMode() {
        ConfigDataBuilder.getInstance()
                .getData()
                .putDebugMode(true)
                .commit();

        assert ConfigDataBuilder.getConfigData().isDebugMode();
    }

    @Test
    public void isCountryItaly() {
        ConfigDataBuilder.getInstance()
                .getData()
                .putCountry("ITALY")
                .commit();

        assert "ITALY".equals(ConfigDataBuilder.getConfigData().getCountry());
    }

    @Test
    public void isLanguageITA() {
        ConfigDataBuilder.getInstance()
                .getData()
                .putLanguage("ITA")
                .commit();

        assert "ITA".equals(ConfigDataBuilder.getConfigData().getCountry());
    }

    @Test
    public void areRegionsSet() {
        String[] regions = new String[]{"Uzbekistan", "Moldavia"};
        ConfigDataBuilder.getInstance()
                .getData()
                .putRegions(regions)
                .commit();

        assert Arrays.equals(regions, ConfigDataBuilder.getConfigData().getRegions());
    }

    @Test
    public void isOkTimeInterval() {
        ConfigDataBuilder.getInstance()
                .getData()
                .putTimeInterval(5)
                .commit();

        assert 5 == ConfigDataBuilder.getConfigData().getTimeInterval();
    }
}
