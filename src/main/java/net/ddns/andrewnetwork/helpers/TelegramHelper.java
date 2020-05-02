package net.ddns.andrewnetwork.helpers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramHelper {

    private static final String TELEGRAM_BOT_CODE = "1099984065:AAEndJQ-osGp_V3ype8raiHUJM44iOqto6Y";
    private static final long TELEGRAM_CHANNEL_ID = -1001218558913L;
    private static TelegramBot bot = new TelegramBot(TELEGRAM_BOT_CODE);


    public static boolean sendMessage(String message) {
        SendResponse response = bot.execute(new SendMessage(getChannelID(), message).parseMode(ParseMode.HTML));
        //test
        return response.isOk();
    }

    public static long getChannelID() {
        return TELEGRAM_CHANNEL_ID;
    }
}
