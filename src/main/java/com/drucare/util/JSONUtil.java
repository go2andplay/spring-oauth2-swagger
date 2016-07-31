package com.drucare.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This utility class is used to convert JSON Payload to POJO vice versa.
 *
 * @author ThirupathiReddy V
 *
 */
public class JSONUtil {

    private JSONUtil() {
    }

    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ObjectMapper PRINT_MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        PRINT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        PRINT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PRINT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> void print(T t) {

        try {
            System.out.println(PRINT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(t));
        } catch (final JsonProcessingException e) {
            LOGGER.debug("Printing ", e);
        }

    }

    public static <T> String json(T t) {

        try {
            return MAPPER.writeValueAsString(t);
        } catch (final JsonProcessingException e) {
            LOGGER.debug("Serialization error  ", e);
        }

        return null;
    }

    public static ObjectMapper jsonMapper() {
        return MAPPER;
    }
}
