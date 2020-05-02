import net.ddns.andrewnetwork.helpers.TelegramHelper;
import org.junit.jupiter.api.Test;

public class TelegramTest {

    @Test
    public void sendSampleMessage() {
        assert TelegramHelper.sendMessage("TEST STRING! SORRY EVERYONE!");
    }
}
