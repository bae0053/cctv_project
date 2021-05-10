package com.example.cctv_tmap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilMethod {
    public String setDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = mFormat.format(date);
        return time;
    }
}
