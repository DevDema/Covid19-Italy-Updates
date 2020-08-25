package net.ddns.andrewnetwork.helpers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramHelper {

    private static final String TELEGRAM_BOT_CODE = "193548529:AAGRkp_3DcjKj-tntnPz1WrraOIA_niOMfc";
    private static long TELEGRAM_CHANNEL_ID;
    private static final TelegramBot bot = new TelegramBot(TELEGRAM_BOT_CODE);


    public static SendResponse sendMessage(String message) {
        return bot.execute(new SendMessage(getChannelID(), message).parseMode(ParseMode.HTML));
    }

    public static BaseResponse editMessage(long messageId, String message) {
        return bot.execute(new EditMessageText(getChannelID(), (int) messageId, message).parseMode(ParseMode.HTML));
    }

    public static BaseResponse deleteMessage(long messageId) {
        return bot.execute(new DeleteMessage(getChannelID(), (int) messageId));
    }

    public static long getChannelID() {
        return TELEGRAM_CHANNEL_ID;
    }

    public static void setChannelId(long telegramChannelId) {
        TELEGRAM_CHANNEL_ID = telegramChannelId;
    }
}
