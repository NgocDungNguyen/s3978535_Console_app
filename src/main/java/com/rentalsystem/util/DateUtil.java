package com.rentalsystem.util;

import org.jline.reader.LineReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date readDate(LineReader reader, String prompt) {
        while (true) {
            String input = reader.readLine(prompt);
            try {
                return DATE_FORMAT.parse(input);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    public static Date readOptionalDate(LineReader reader, String prompt) {
        String input = reader.readLine(prompt);
        if (input.isEmpty()) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(input);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }
}