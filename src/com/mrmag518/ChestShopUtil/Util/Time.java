package com.mrmag518.ChestShopUtil.Util;

/**
 * Time utility class by mrmag518.
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Time {
    private int hour;
    private int minute;
    private int second;
    private int millisecond;
    private int year;
    private Date date;
    
    public Time() {
        Calendar calendar = Calendar.getInstance();
        this.second = calendar.get(Calendar.SECOND);
        this.minute = calendar.get(Calendar.MINUTE);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.millisecond = calendar.get(Calendar.MILLISECOND);
        this.year = calendar.get(Calendar.YEAR);
        this.date = calendar.getTime();
    }
    
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }
    
    public int getMillisecond() {
        return millisecond;
    }
    
    public int getYear() {
        return year;
    }
    
    public String getClock() {
        return hour + ":" + minute + ":" + second;
    }
    
    public Date getDate() {
        return date;
    }
    
    public Date parseDate(String simpleDateFormat) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            return dateFormat.parse(simpleDateFormat);
        } catch (ParseException ex) {
            Logger.getLogger(Time.class.getName()).log(Level.SEVERE, "An error ocurred when trying to parse a simpledateformat into a date.", ex);
        }
        return null;
    }
    
    public String getSimpleDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }
    
    public boolean isAfter(Date checkDate) {
        return date.after(checkDate);
    }
    
    public boolean isBefore(Date checkDate) {
        return date.before(checkDate);
    }
    
    public int daysBetween(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        long ms1 = c1.getTimeInMillis();
        long ms2 = c2.getTimeInMillis();
        long diff = ms2 - ms1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        
        return (int) diffDays;
    }
    
    public int getDaysFromMS(long ms) {
        return (int) TimeUnit.MILLISECONDS.toDays(ms);
    }
    
    public int getHoursFromMS(long ms) {
        return (int) TimeUnit.MILLISECONDS.toHours(ms);
    }
}
