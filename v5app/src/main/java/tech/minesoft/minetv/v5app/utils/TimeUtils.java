package tech.minesoft.minetv.v5app.utils;

public class TimeUtils {

    public static String hh_mm_ss(long ms) {
        ms = ms/1000;

        long second = ms % 60;
        ms = ms / 60;

        long minute = ms % 60;
        long hour = ms / 60;

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%02d", hour));
        sb.append(":");
        sb.append(String.format("%02d", minute));
        sb.append(":");
        sb.append(String.format("%02d", second));

        return sb.toString();
    }
}
