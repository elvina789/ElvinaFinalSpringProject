package com.jb.ElvinaFinalSpringProject.utils;

import com.google.gson.Gson;

public class EncodingUtil {
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return new Gson().fromJson(json, type);
    }
}
