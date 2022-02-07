package tech.minesoft.minetv.utils;

import java.util.HashMap;
import java.util.Map;

public class ListUtils {

    public static Map<String, String> split2Map(String[] keys, String[] vals) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], vals[i]);
        }
        return map;
    }

}
