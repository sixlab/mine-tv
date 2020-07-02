package com.ubtv66.minetv.data;

import com.ubtv66.minetv.vo.VodInfo;

import java.util.ArrayList;
import java.util.List;

public class VodData {
    private static String[] names={
    };
    private static String[] urls={
    };

    public static List<VodInfo> getList() {
        List<VodInfo> list = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            VodInfo vodInfo = new VodInfo();
            vodInfo.setId(i);
            vodInfo.setVodPic("");
            vodInfo.setVodName(names[i]);
            vodInfo.setVodIntro(urls[i]);

            list.add(vodInfo);
        }

        return list;
    }
}
