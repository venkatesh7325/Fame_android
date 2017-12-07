package com.purpledot.fame.utils;

import java.util.Date;

/**
 * Created by hp  pc on 13-05-2017.
 */

public class Constants {

    public static final String CLASSTIME_PREF = "ADMIN_PREF";
    public static final String MAIN_URL = "";
    public static final String no_internet_connection = "Please check your connection and try again.";
    public static final String something_went_wrong = "Some thing went wrong";
    public static final String STATISTICS_URL = "http://worldonhire.com/fame-dashboard/html/fame/index.php?";

    public static Date CURRENT_DATE;
    public static Date CURRENT_TIME;

    // http://maayog.com/live.html

    public static String getYearId(String year) {
        String yearID = "";
        switch (year) {
            case "1 Year":
                yearID = "1";
                break;
            case "2nd Year":
                yearID = "2";
                break;
            case "3rd Year":
                yearID = "3";
                break;
            case "4th Year":
                yearID = "4";
                break;
            case "I":
                yearID = "1";
                break;
            case "II":
                yearID = "2";
                break;
        }
        return yearID;
    }

}
