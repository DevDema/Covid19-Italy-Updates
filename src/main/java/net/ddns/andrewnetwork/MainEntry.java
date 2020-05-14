package net.ddns.andrewnetwork;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.util.ListUtils;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigSavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.ConfigData;
import net.ddns.andrewnetwork.model.ConfigSavedData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainEntry {
    private static long DELAY = 5 * 60 * 1000L; // 5 MINUTES
    private static boolean DEBUG_MODE = false;
    private static final String OPTION_PATTERN = "-[a-z,A-Z]"; //pattern for specified option
    private static final MainPresenter MAIN_PRESENTER = new MainPresenter();

    private static String[] regionsData;
    private static String language;
    private static boolean daemonMode = false;


    public static void main(String[] args) {
        String requiredData = getRequiredData(args);

        getOptionalData(args);
        ConfigData configData = ConfigDataBuilder.getConfigData();
        TelegramHelper.setChannelId(configData.getChannelID());

        if (daemonMode) {
            while (true) {
                getData();

                try {
                    if (DEBUG_MODE) {
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, DELAY / 1000));
                    }
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(e.getMessage());
                    break;
                }
            }
        } else {
            getData();
        }


    }

    private static void getData() {
        if (DEBUG_MODE) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("getting Data... Last Update:" +
                    (ConfigDataBuilder.getConfigData() != null && ConfigDataBuilder.getConfigData().getLastDay() != null ? ConfigDataBuilder.getConfigData().getLastDay().getDate() : "Never"));
        }

        MAIN_PRESENTER.getData(regionsData);
    }

    public static void onDataLoaded(CovidItaData covidItaData, Set<CovidRegionData> covidRegionData) {
        ConfigSavedData configSavedData = ConfigDataBuilder.getConfigData().getLastDay();
        CovidItaData covidItaSavedData = configSavedData != null ? configSavedData.getItalyDataSaved() : new CovidItaData();
        Set<CovidRegionData> covidRegionDataSavedList = configSavedData != null
                ? new HashSet<>(configSavedData.getRegionsDataSaved())
                : new HashSet<>();

        Set<Date> newDates = covidRegionData.stream().map(CovidItaData::getDate).collect(Collectors.toSet());
        newDates.add(covidItaData.getDate());

        if (covidItaSavedData.getDate() == null || DateUtil.isTomorrowDay(DateUtil.max(newDates), configSavedData.getDate())) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("New data found! Date: " + DateUtil.max(newDates) + ". Updating...");

            sendNewMessage(covidItaData, covidRegionData);
        } else if (!covidItaData.equals(covidItaSavedData) || !covidRegionData.equals(covidRegionDataSavedList)) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data were edited! Updating...");
            editLastMessage(covidItaData, covidRegionData);
        } else {
            if (DEBUG_MODE) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data is not updated. Skipping...");
            }
        }
    }

    private static void editLastMessage(CovidItaData covidItaData, Collection<CovidRegionData> covidRegionData) {
        long lastMessageId = ConfigDataBuilder.getConfigData().getMessageID();

        BaseResponse baseResponse = TelegramHelper.editMessage((int) lastMessageId, StringConfig.buildFinalMessage(covidItaData.getDate(), covidItaData, covidRegionData));

        if (!baseResponse.isOk()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Telegram editing FAILED! Error Code: " + baseResponse.errorCode());
            return;
        }

        ConfigDataBuilder.getInstance()
                .getData()
                .putDays(ConfigSavedDataBuilder.getInstance()
                        .getLastData()
                        .putTodayData(covidItaData, covidRegionData)
                        .build()
                )
                .putMessageId(lastMessageId)
                .commit();
    }

    private static void sendNewMessage(CovidItaData covidItaData, Collection<CovidRegionData> covidRegionData) {
        SendResponse sendResponse = TelegramHelper.sendMessage(
                StringConfig.buildFinalMessage(covidItaData.getDate(), covidItaData, covidRegionData)
        );

        if (!sendResponse.isOk()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Telegram sending FAILED! Error Code: " + sendResponse.errorCode());
            return;
        }

        Message message = sendResponse.message();

        if (message == null) {
            return;
        }

        ConfigDataBuilder.getInstance()
                .getData()
                .putDays(ConfigSavedDataBuilder.getInstance()
                        .newData()
                        .putTodayData(covidItaData, covidRegionData)
                        .build()
                )
                .putMessageId(message.messageId())
                .commit();
    }

    private static List<Integer> getOptionPositions(String[] args) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].matches(OPTION_PATTERN) && args[i].length() == 2) {
                list.add(i);
            }
        }

        list.add(args.length);

        return list;
    }

    private static void getOptionalData(String[] args) {
        List<Integer> optionsIndex = getOptionPositions(args);

        for (int i = 0; i < optionsIndex.size() - 1; i++) {
            char option = args[optionsIndex.get(i)].charAt(1);
            String[] optionsData = ListUtils.getSubArray(args, optionsIndex.get(i) + 1, optionsIndex.get(i + 1));

            switch (option) {
                case 'r':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS_OPTION + option);
                    }

                    regionsData = optionsData;
                    break;
                case 'l':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS_OPTION + option);
                    }

                    if (optionsData.length > 1) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MULTIPLE_LANGUAGES);
                    }

                    if (!optionsData[0].equalsIgnoreCase("ITA")) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_UNSUPPORTED_LANGUAGE);
                    }

                    language = optionsData[0];
                    break;
                case 'd':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS_OPTION + option);
                    }

                    if (optionsData.length > 1) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 1));
                    }

                    try {
                        if (optionsData[0].contains("*") || optionsData[0].contains("+")
                                || optionsData[0].contains("-") || optionsData[0].contains("/")) {
                            throw new IllegalArgumentException(StringConfig.EXCEPTION_OPTION_NUMBER_FORMAT + option);
                        }

                        DELAY = Integer.parseInt(optionsData[0].replaceAll("[^\\d.]", "")) * 1000;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_OPTION_NUMBER_FORMAT + option);
                    }
                    break;
                case 'D':
                    if (optionsData.length > 0) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 0));
                    }

                    DEBUG_MODE = true;
                    break;
                case 's':
                    if (optionsData.length > 0) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 0));
                    }

                    daemonMode = true;
                    break;
                case 'c':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS_OPTION + option);
                    }

                    if (optionsData.length > 1) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 1));
                    }

                    ConfigDataBuilder.setConfigPath(optionsData[0]);
                    break;
                default:
                    throw new IllegalArgumentException(StringConfig.EXCEPTION_UNRECOGNIZED + option);
            }
        }

        if (language == null) {
            language = "ITA";
        }

        if (regionsData == null) {
            regionsData = new String[]{"Lombardia"};
        }
    }

    private static String getRequiredData(String[] args) {
        if (args.length > 0) {
            String country = args[0];

            switch (country) {
                case "ITALY":
                    return country;
                default:
                    throw new IllegalArgumentException(StringConfig.EXCEPTION_INVALID_COUNTRY);
            }
        }

        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS);
    }
}
