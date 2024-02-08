package org.globallogic.gorest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTimeFormatter {

    public String returnCurrentDateAndTime() {
        DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateTime.format(date);
    }
}
