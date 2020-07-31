package tech.minesoft.minetv.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

    public static List<List> splitList(List list, int groupSize) {
        int length = list.size();

        // 计算可以分成多少组
        int num = (length + groupSize - 1) / groupSize;

        List<List> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
            newList.add(list.subList(fromIndex, toIndex));
        }
        return newList;
    }

    public static Map<String, String> split2Map(String[] keys, String[] vals) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], vals[i]);
        }
        return map;
    }

}
