package net.ddns.andrewnetwork.model;

import com.google.gson.annotations.SerializedName;

public class TemporaryConfigData {

    @SerializedName("last_message_id")
    private long messageID;

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }
}
