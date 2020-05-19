import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.SendResponse;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class TelegramTest {

    private static final Set<Long> messagesToBeDeleted = new HashSet<>();
    @BeforeAll
    public static void setup() {
        TelegramHelper.setChannelId(-1001446903259L);
    }

    @AfterAll
    public static void setupAfter() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (long message : messagesToBeDeleted) {
            TelegramHelper.deleteMessage(message);
        }
    }

    @Test
    public void sendSampleMessage() {
        SendResponse sendResponse = TelegramHelper.sendMessage("TEST STRING! SORRY EVERYONE!");

        if(!sendResponse.isOk()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(sendResponse.errorCode() + " " + sendResponse.description());
        }

        assert sendResponse.isOk();

        messagesToBeDeleted.add(Long.valueOf(sendResponse.message().messageId()));
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

        messagesToBeDeleted.add(Long.valueOf(sendResponse.message().messageId()));
    }

    @Test
    public void sendEmoji() {
        String italyEmoji = StringConfig.getItalyEmoji();

        SendResponse sendResponse = TelegramHelper.sendMessage(italyEmoji);

        assert sendResponse.isOk();

        messagesToBeDeleted.add(Long.valueOf(sendResponse.message().messageId()));
    }
}
