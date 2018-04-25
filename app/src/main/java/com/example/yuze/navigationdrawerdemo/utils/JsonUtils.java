package com.example.yuze.navigationdrawerdemo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    /**
     * object mapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * write an object to corresponding json string
     *
     * @param o the object you want to write to json
     * @return return json string if succeed, otherwise return null
     */
    public static String write(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * read a json string to corresponding class instance
     *
     * @param clazz the type of you want to read
     * @param json  the json string you want to read from
     * @return return instance if succeed, otherwise return null
     */
    public static <T> T read(final String json, final Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            return null;
        }
    }
}
