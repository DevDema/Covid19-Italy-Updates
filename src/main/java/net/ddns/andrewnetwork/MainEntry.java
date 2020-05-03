package net.ddns.andrewnetwork;

import net.ddns.andrewnetwork.helpers.ConfigHelper;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.util.DateUtil;
import net.ddns.andrewnetwork.helpers.util.ListUtils;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class MainEntry {
    private static long DELAY_LONG = 2*60*60*1000L; // 2 HOURS
    private static long DELAY_SHORT = 15*60*1000L; // 15 MINUTES
    private static int INTERVAL_SHORT_DELAY_MINUTES = 120; // 2 HOURS
    private static boolean DEBUG_MODE = false;
    private static final String OPTION_PATTERN = "-[a-z,A-Z]";
    private static final MainPresenter MAIN_PRESENTER = new MainPresenter();

    private static String[] regionsData;
    private static String language;
    private static Date date;
    private static boolean daemonMode = false;


    public static void main(String[] args) {
        String requiredData = getRequiredData(args);

        getOptionalData(args);
        date = ConfigHelper.getConfigData().getDate();
        if(daemonMode) {
            while (true) {
                long nowTime = System.currentTimeMillis();
                long referenceTime = date.getTime();
                long twoHourAfterTime = DateUtil.minutesAfter(date, INTERVAL_SHORT_DELAY_MINUTES).getTime();

                getData();

                try {
                    if(DEBUG_MODE) {
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(StringConfig.DAEMON_MODE_WAITING);
                    }
                    Thread.sleep(nowTime > referenceTime && nowTime < twoHourAfterTime ? DELAY_SHORT : DELAY_LONG);
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
        if(DEBUG_MODE) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("getting Data..." + date);
        }

        MAIN_PRESENTER.getData(regionsData);
    }

    public static void onDataLoaded(CovidItaData covidItaData, List<CovidRegionData> covidRegionData) {
        if(covidItaData.getDate().after(date)) {
            try {
                TelegramHelper.sendMessage(StringConfig.buildFinalMessage(covidItaData.getDate(), covidItaData, covidRegionData));
            } catch (Exception e) {
                e.printStackTrace();
            }

            date = covidItaData.getDate();
            ConfigHelper.putRowData(covidItaData.getDate());
        } else {
            if(DEBUG_MODE) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data is not updated.");
            }
        }
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

                    if (optionsData.length < 2) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_DELAY_MISSING);
                    }

                    if (optionsData.length > 2) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 2));
                    }

                    try {
                        DELAY_LONG = Integer.parseInt(optionsData[0]) * 1000;
                        DELAY_SHORT = Integer.parseInt(optionsData[1]) * 1000;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_OPTION_NUMBER_FORMAT + option);
                    }
                    break;
                case 't':
                    if (optionsData.length == 0) {
                        throw new IllegalArgumentException(StringConfig.EXCEPTION_MISSING_ARGUMENTS_OPTION + option);
                    }

                    if (optionsData.length > 1) {
                        throw new IllegalArgumentException(String.format(StringConfig.EXCEPTION_MANY_ARGS_OPTION, option, 1));
                    }

                    try {
                        INTERVAL_SHORT_DELAY_MINUTES = Integer.parseInt(optionsData[0]);
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
                default:
                    throw new IllegalArgumentException(StringConfig.EXCEPTION_UNRECOGNIZED + option);
            }
        }

        if (language == null) {
            language = "EN";
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
