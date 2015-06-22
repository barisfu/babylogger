package com.rorlig.babylog.otto.events.milestones;

/**
 * Created by rorlig on 6/11/15.
 */
public class MilestoneCancelEvent {

    private final int year;
    private final int month;
    private final int day;
    int position;

    public MilestoneCancelEvent(int position, int year, int month, int day) {
        this.position = position;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getPosition() {
        return position;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
