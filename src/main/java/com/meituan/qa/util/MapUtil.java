package com.meituan.qa.util;

import com.meituan.qa.bean.AppkeyData;

import java.util.Map;
import java.util.TreeMap;

public class MapUtil {
    public static Map<String, AppkeyData> sortMapByKey(Map<String, AppkeyData> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, AppkeyData> sortMap = new TreeMap<String, AppkeyData>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}
