package tech.minesoft.minetv.data;

import android.content.Context;

import tech.minesoft.minetv.vo.VodInfo;

public class SampleData {
    private static String[] names = {
            "m3u8",
            "mp4",
    };
    private static String[] urls = {
            "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8",
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
    };

    private static VodInfo getDate() {
        VodInfo vodInfo = new VodInfo();

        vodInfo.setVod_id(-1);

        vodInfo.setVod_year("2020");
        vodInfo.setVod_name("影视");
        vodInfo.setType_name("type");

        vodInfo.setVod_director("director");
        vodInfo.setVod_actor("actor");

        vodInfo.setVod_pic("");

        vodInfo.setVod_play_from("local");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            sb.append(names[i]).append("$").append(urls[i]).append("#");
        }
        sb.deleteCharAt(sb.length() - 1);

        vodInfo.setVod_play_url(sb.toString());

        return vodInfo;
    }

    public static void initSampleData(Context context) {
        DbHelper.insertStar(context, getDate());
    }
}