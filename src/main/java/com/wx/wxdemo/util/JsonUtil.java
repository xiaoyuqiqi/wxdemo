package com.wx.wxdemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private ValueFilter filter = new ValueFilter() {
        public Object process(Object obj, String s, Object v) {
            return v == null ? "" : v;
        }
    };

    public JsonUtil() {
    }

    public static String toJsonP(String prefix, Object obj) {
        return toJsonP(prefix, obj);
    }

    public static String toJsonP(String prefix, Object obj, String excludeStr) {
        return toJsonP(prefix, obj, excludeStr);
    }

    public static String toJson(Object obj) {
        SerializeWriter out = new SerializeWriter();

        String jsonStr;
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.WriteEnumUsingToString, false);
            serializer.config(SerializerFeature.WriteEnumUsingName, false);
            serializer.config(SerializerFeature.WriteNullListAsEmpty, true);
            serializer.config(SerializerFeature.WriteNullStringAsEmpty, true);
            serializer.config(SerializerFeature.WriteMapNullValue, true);
            ValueFilter filter = new ValueFilter() {
                public Object process(Object obj, String s, Object v) {
                    return v == null ? "" : v;
                }
            };
            serializer.getValueFilters().add(filter);
            serializer.write(obj);
            jsonStr = out.toString();
        } finally {
            out.close();
        }

        return jsonStr;
    }

    public static <T> T toObject(String jsonStr, Class<T> clzss) {
        return JSON.parseObject(jsonStr, clzss);
    }

    public static <T> List<T> toArray(String jsonStr, Class<T> clzss) {
        return JSON.parseArray(jsonStr, clzss);
    }

    public static List<Object> toArray(String jsonStr) {
        return JSON.parseArray(jsonStr, Object.class);
    }

    public static Object toObject(String jsonStr) {
        return toObject(jsonStr, Object.class);
    }

    public static List<Map<String, Object>> toListMap(String jsonStr) {
        List<Object> list = JSON.parseArray(jsonStr);
        List<Map<String, Object>> listNew = new ArrayList();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            Object object = var3.next();
            Map<String, Object> map = (Map)object;
            listNew.add(map);
        }

        return listNew;
    }
}
