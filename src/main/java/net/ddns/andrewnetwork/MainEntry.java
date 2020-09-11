package net.ddns.andrewnetwork;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.util.ListUtils;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataDayBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.ConfigData;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import net.ddns.andrewnetwork.model.SavedDataDay;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainEntry {
    private static long DELAY;
    private static Boolean DEBUG_MODE;
    private static Boolean DAEMON_MODE;
    private static final String OPTION_PATTERN = "-[a-z,A-Z]"; //pattern for specified option
    private static final MainPresenter MAIN_PRESENTER = new MainPresenter();

    private static String[] regionsData;
    private static String language;
    private static String country;


    public static void main(String[] args) {
        setupConfiguration(args);

        if (DAEMON_MODE) {
            while (true) {
                getData();

                try {
                    if (DEBUG_MODE) {
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(String.format(StringConfig.DAEMON_MODE_WAITING, (int) DELAY / 1000));
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
                    (SavedDataBuilder.getSavedData() != null && SavedDataBuilder.getSavedData().getLastDay() != null ? SavedDataBuilder.getSavedData().getLastDay().getDate() : "Never"));
        }

        MAIN_PRESENTER.getData(regionsData);
    }

    public static boolean onDataLoaded(CovidItaData covidItaData, Set<CovidRegionData> covidRegionData) {
        SavedDataDay savedDataDay = SavedDataBuilder.getSavedData().getLastDay();
        CovidItaData covidItaSavedData = savedDataDay != null ? savedDataDay.getItalyDataSaved() : new CovidItaData();
        Set<CovidRegionData> covidRegionDataSavedList = savedDataDay != null
                ? new HashSet<>(savedDataDay.getRegionsDataSaved())
                : new HashSet<>();

        Set<Date> newDates = covidRegionData.stream().map(CovidItaData::getDate).collect(Collectors.toSet());
        newDates.add(covidItaData.getDate());

        boolean areDatesEqual = DateUtil.areSameDays(newDates);

        if (!areDatesEqual) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Dates are different. Seems like a partial update. Skipping...");
            return false;
        }

        if (savedDataDay == null || covidItaSavedData.getDate() == null ||
                DateUtil.isTomorrowDay(covidItaData.getDate(), savedDataDay.getDate())) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("New data found! Date: " + DateUtil.max(newDates) + ". Updating...");

            sendNewMessage(covidItaData, covidRegionData);
            return true;
        } else if (!covidItaData.equals(covidItaSavedData) || !covidRegionData.equals(covidRegionDataSavedList)) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data were edited! Updating...");
            editLastMessage(covidItaData, covidRegionData);
            return true;
        } else {
            if (DEBUG_MODE) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data is not updated. Skipping...");
            }
        }

        return false;
    }

    private static void editLastMessage(CovidItaData covidItaData, Collection<CovidRegionData> covidRegionData) {
        long lastMessageId = ConfigDataBuilder.getConfigData().getTemporaryData().getMessageID();

        BaseResponse baseResponse = TelegramHelper.editMessage((int) lastMessageId, StringConfig.buildFinalMessage(covidItaData.getDate(), covidItaData, covidRegionData));

        if (!baseResponse.isOk()) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Telegram editing FAILED! Error Code: " + baseResponse.errorCode());
            return;
        }

        SavedDataBuilder.getInstance()
                .getData()
                .putDays(SavedDataDayBuilder.getInstance()
                        .getLastData()
                        .putTodayData(covidItaData, covidRegionData)
                        .build()
                )
                .commit();

        ConfigDataBuilder.getInstance()
                .getData()
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

        SavedDataBuilder.getInstance()
                .getData()
                .putDays(SavedDataDayBuilder.getInstance()
                        .newData()
                        .putTodayData(covidItaData, covidRegionData)
                        .build()
                )
                .commit();

        ConfigDataBuilder.getInstance()
                .getData()
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

    private static void setupConfiguration(String[] args) {
        List<Integer> optionsIndex = getOptionPositions(args);

        if (args.length == 0) {
            throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS);
        }

        if (!(Arrays.stream(args).filter(arg -> arg.trim().equals("-c")).count() == 1) || !(Arrays.stream(args).filter(arg -> arg.trim().equals("-o")).count() == 1)) {
            throw new IllegalArgumentException("Configuration and Data Output files are required.");
        }

        for (int i = 0; i < optionsIndex.size() - 1; i++) {
            char option = args[optionsIndex.get(i)].charAt(1);
            String[] optionsData = ListUtils.getSubArray(args, optionsIndex.get(i) + 1, optionsIndex.get(i + 1));
            switch (option) {
                case 'C':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS + option);
                    }

                    if (optionsData.length > 1) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MANY_ARGS_OPTION);
                    }

                    country = optionsData[0];

                    if (country == null || country.isEmpty()) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_INVALID_COUNTRY);
                    }

                    if (!"ITALY".equals(country)) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_INVALID_COUNTRY);
                    }

                    break;
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

                    DAEMON_MODE = true;
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
                case 'o':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS_OPTION + option);
                    }

                    if (optionsData.length > 1) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 1));
                    }

                    SavedDataBuilder.setSavedDataPath(optionsData[0]);
                    break;
                default:
                    throw new IllegalArgumentException(StringConfig.EXCEPTION_UNRECOGNIZED + option);
            }


            if (!Arrays.stream(args).filter(arg -> arg.matches(OPTION_PATTERN)).allMatch(new HashSet<>()::add)) {
                throw new IllegalArgumentException(StringConfig.EXCEPTION_DUPLICATE_OPTION);
            }

            String configPath = ConfigDataBuilder.getConfigPath();
            if (configPath == null || configPath.isEmpty()) {
                throw new IllegalArgumentException("Missing config path.");
            }

            ConfigData configData = ConfigDataBuilder.getConfigData();

            if (configData.getChannelID() == 0) {
                throw new IllegalArgumentException("A channel Id must be provided");
            }

            if (DAEMON_MODE == null) {
                DAEMON_MODE = configData.isDaemonMode();
            }

            if (DEBUG_MODE == null) {
                DEBUG_MODE = configData.isDebugMode();
            }

            if (language == null || language.isEmpty()) {
                language = configData.getLanguage();
            }

            if (country == null || country.isEmpty()) {
                country = configData.getCountry();
            }

            if (regionsData == null || regionsData.length == 0) {
                regionsData = configData.getRegions();
            }

            if (DELAY == 0) {
                DELAY = configData.getTimeInterval() * 60 * 1000L;
            }
            TelegramHelper.setChannelId(configData.getChannelID());
        }

        if (DAEMON_MODE == null) {
            DAEMON_MODE = false;
        }

        if (DEBUG_MODE == null) {
            DEBUG_MODE = false;
        }

        if (language == null) {
            language = "ITA";
        }

        if (DELAY == 0) {
            DELAY = 5 * 60 * 1000L; //5 minutes
        }

        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException(StringConfig.EXCEPTION_INVALID_COUNTRY);
        }

        if (regionsData == null || regionsData.length == 0) {
            throw new IllegalArgumentException("Regions must be provided.");
        }
    }

    //used in tests to reset static context.
    public static void reset() {
        regionsData = null;
        language = null;
        country = null;
        DEBUG_MODE = null;
        DAEMON_MODE = null;
        DELAY = 5 * 60 * 1000L; // 5 MINUTES
    }
}
