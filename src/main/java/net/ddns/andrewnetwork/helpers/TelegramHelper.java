package net.ddns.andrewnetwork.helpers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramHelper {

    private static final String TELEGRAM_BOT_CODE = "1099984065:AAEndJQ-osGp_V3ype8raiHUJM44iOqto6Y";
    private static long TELEGRAM_CHANNEL_ID;
    private static TelegramBot bot = new TelegramBot(TELEGRAM_BOT_CODE);


    public static SendResponse sendMessage(String message) {
        return bot.execute(new SendMessage(getChannelID(), message).parseMode(ParseMode.HTML));
    }

    public static BaseResponse editMessage(int messageId, String message) {
        return bot.execute(new EditMessageText(getChannelID(), messageId, message).parseMode(ParseMode.HTML));
    }

    public static long getChannelID() {
        return TELEGRAM_CHANNEL_ID;
    }

    public static void setChannelId(long telegramChannelId) {
        TELEGRAM_CHANNEL_ID = telegramChannelId;
    }
}
