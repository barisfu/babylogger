package com.rorlig.babyapp.parse_dao;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.rorlig.babyapp.dao.BaseDao;

import java.util.Date;

/**
 * @author gaurav gupta
 * Sleep Dao
 */
@ParseClassName("Parse")
public class Sleep extends ParseObject {





    Date sleepStartTime;

    Long duration;

    //time at which the log was changed ...
    Date logCreationDate;

    public Sleep() {
    }

    public Sleep(Date sleepStartTime,
                 Long duration, Date date) {
        this.sleepStartTime = sleepStartTime;
        this.duration = duration;
        this.logCreationDate = new Date(date.getTime());
    }



    public Date getSleepStartTime() {
        return getDate("sleepStartTime");
    }

    public void setSleepStartTime(Date sleepStartTime) {
        put("sleepStartTime", sleepStartTime);
    }

    public Long getDuration() {
        return getLong("duration");
    }

    public void setDuration(Long duration) {
        put("duration", duration);
    }

    public Date getLogCreationDate() {
        return getDate("logCreationDate");
    }

    public void setLogCreationDate(Date logCreationDate) {
        put("logCreationDate", logCreationDate);
    }


    @Override
    public String toString() {
        return "Sleep{" +
                "sleepStartTime=" + sleepStartTime +
                ", duration=" + duration +
                ", logCreationDate=" + logCreationDate +
                "} " + super.toString();
    }
}
