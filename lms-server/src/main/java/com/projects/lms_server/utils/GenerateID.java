package com.projects.lms_server.utils;

import java.util.Random;

public class GenerateID {
    public  static String generateId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0  ; i < 6 ; i++) {
            int val = random.nextInt(10);
            sb.append(val);
        }
        return sb.toString();
    }
}
