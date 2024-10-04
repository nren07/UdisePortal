package com.udise.portal.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion {
    public static void main(String[] args) {
        String dateString = "2024-10-02"; // Example date string
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Define the format

        try {
            Date date = formatter.parse(dateString); // Convert String to Date
            System.out.println("Converted Date: " + date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception
        }
    }
}