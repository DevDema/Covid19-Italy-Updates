package net.ddns.andrewnetwork;

import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.util.ListUtils;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.ArrayList;
import java.util.List;

public class MainEntry {
    private static final String OPTION_PATTERN = "-[a-z,A-Z]";
    private static final MainPresenter MAIN_PRESENTER = new MainPresenter();

    private static String[] regionsData;
    private static String language;

    public static void main(String[] args) {
        String requiredData = getRequiredData(args);
        getOptionalData(args);

        MAIN_PRESENTER.getData(regionsData);
    }

    public static void onDataLoaded(CovidItaData covidItaData, List<CovidRegionData> covidRegionData) {
        try {
            TelegramHelper.sendMessage(StringConfig.buildFinalMessage(covidItaData.getDate(), covidItaData, covidRegionData));
        } catch (Exception e) {
            e.printStackTrace();
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
            char option = args[optionsIndex.get(i)].toLowerCase().charAt(1);
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
