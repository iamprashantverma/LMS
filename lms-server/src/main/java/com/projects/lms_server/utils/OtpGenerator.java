package com.projects.lms_server.utils;

import java.util.Random;

public class OtpGenerator {
    public static String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a digit between 0-9
            otp.append(digit);
        }

        return otp.toString();
    }
}
