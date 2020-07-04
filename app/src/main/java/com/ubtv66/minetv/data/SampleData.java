package com.ubtv66.minetv.data;

import com.ubtv66.minetv.vo.VodInfo;

public class SampleData {
    private static String[] names = {
    };
    private static String[] urls = {
    };

    public static VodInfo getDate() {
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
}