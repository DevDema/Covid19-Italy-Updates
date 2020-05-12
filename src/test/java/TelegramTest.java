import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

public class TelegramTest {

    @BeforeAll
    public static void setup() {
        TelegramHelper.setChannelId(-1001446903259L);
    }

    @Test
    public void sendSampleMessage() {
        SendResponse sendResponse = TelegramHelper.sendMessage("TEST STRING! SORRY EVERYONE!");

        if(!sendResponse.isOk()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(sendResponse.errorCode() + " " + sendResponse.description());
        }

        assert sendResponse.isOk();
    }

    @Test
    public void editSampleMessage() {
        SendResponse sendResponse = TelegramHelper.sendMessage("TEST STRING!");

        if(!sendResponse.isOk()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(sendResponse.errorCode() + " " + sendResponse.description());
        }

        assert sendResponse.isOk();

        Message message = sendResponse.message();
        assert TelegramHelper.editMessage(message.messageId(), "TEST STRING! EDITED VERSION!").isOk();
    }
}
