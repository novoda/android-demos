package com.novoda.example.compass.utils;

public class CompassUtils {

    public static String getDirectionFromBearing(double bearing) {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";
        if (range == 15 || range == 0)
            dirTxt = "N";
        else if (range == 1 || range == 2)
            dirTxt = "NE";
        else if (range == 3 || range == 4)
            dirTxt = "E";
        else if (range == 5 || range == 6)
            dirTxt = "SE";
        else if (range == 7 || range == 8)
            dirTxt = "S";
        else if (range == 9 || range == 10)
            dirTxt = "SW";
        else if (range == 11 || range == 12)
            dirTxt = "W";
        else if (range == 13 || range == 14)
            dirTxt = "NW";
        return dirTxt;
    }
    
    public static int getRotationDegreesFromBearing(double bearing){
        return  (int)(360 - bearing);
    }
    
}
