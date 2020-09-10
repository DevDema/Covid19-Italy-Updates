package net.ddns.andrewnetwork.helpers.util.builder;

import net.ddns.andrewnetwork.helpers.util.FileUtils;
import net.ddns.andrewnetwork.helpers.util.JsonUtil;
import net.ddns.andrewnetwork.model.SavedData;
import net.ddns.andrewnetwork.model.SavedDataDay;

import java.util.Collection;

public class SavedDataBuilder {

    private static final SavedDataBuilder savedDataHelper = new SavedDataBuilder();
    private static String SAVED_DATA_PATH;
    private SavedData savedData;

    public static void clear() {
        SavedDataBuilder.getInstance()
                .newData()
                .commit();
    }

    public static String getSavedDataString() {
        if (SAVED_DATA_PATH == null || SAVED_DATA_PATH.isEmpty()) {
            throw new IllegalArgumentException("Accessing SAVED_DATA_PATH without setting it first.");
        }

        return FileUtils.getFile(SAVED_DATA_PATH);
    }

    public static SavedData getSavedData() {
        String fileAsString = getSavedDataString();

        return JsonUtil.getGson().fromJson(fileAsString, SavedData.class);
    }

    public static void setSavedDataPath(String savedDataPath) {
        SAVED_DATA_PATH = savedDataPath;
    }

    public static SavedDataBuilder getInstance() {
        return savedDataHelper;
    }

    public SavedDataBuilder putDays(Collection<SavedDataDay> savedDatumDays) {
        if (savedData == null) {
            throw new IllegalStateException("putDays() called without calling getData() first.");
        }

        savedData.setLastDays(savedDatumDays);

        return this;
    }

    private SavedDataBuilder newData() {
        this.savedData = new SavedData();

        return this;
    }

    public SavedDataBuilder getData() {
        this.savedData = getSavedData();

        return this;
    }

    public void commit() {
        if (SAVED_DATA_PATH == null || SAVED_DATA_PATH.isEmpty()) {
            throw new IllegalArgumentException("Accessing SAVED_DATA_PATH without setting it first.");
        }

        if (savedData == null) {
            throw new IllegalStateException("commit() called without calling getData() first.");
        }

        FileUtils.writeFile(SAVED_DATA_PATH, JsonUtil.getGson().toJson(savedData));

        savedData = null;
    }


}
