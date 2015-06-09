package com.rorlig.babylog.model.feed;

/**
 * Created by gaurav
 */
public enum FeedType {

    BOTTLE ("bottle"), BREAST ("breast");

    private final String value;

    FeedType(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return getValue();
    }
}
