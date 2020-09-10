package net.ddns.andrewnetwork.model;

import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;

import java.util.Collection;
import java.util.Date;

public class SavedData {

    private Collection<SavedDataDay> lastDays;

    public Collection<SavedDataDay> getLastDays() {
        return lastDays;
    }

    public void setLastDays(Collection<SavedDataDay> lastDays) {
        this.lastDays = lastDays;
    }

    public SavedDataDay getLastDay() {
        return CovidDataUtils.getMostRecentDate(lastDays);
    }

    public SavedDataDay getDayBy(Date date) {
        return lastDays
                .stream()
                .filter(configSavedData -> configSavedData.getDate() != null && DateUtil.isSameDay(configSavedData.getDate(), date))
                .findFirst()
                .orElse(null);
    }
}
