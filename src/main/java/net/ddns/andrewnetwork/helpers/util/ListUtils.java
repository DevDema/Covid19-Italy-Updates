package net.ddns.andrewnetwork.helpers.util;

import net.ddns.andrewnetwork.model.CovidRegionData;

import java.util.List;

public class ListUtils {

    public static String[] getSubArray(String[] array, int begin, int end) {
        if (begin > end) {
            throw new IllegalArgumentException("End less than Begin in array.");
        }

        int size = end - begin;
        String[] newArray = new String[size];

        int j = 0;
        for (int i = begin; i < end; i++) {
            newArray[j] = array[i];
            j++;
        }

        return newArray;
    }
}
