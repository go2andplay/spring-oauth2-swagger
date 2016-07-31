package com.drucare.util;

import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to prepares basic authentication token
 *
 * @author ThirupathiReddy V
 *
 */
public class TokenGeneratorUtil {

    private TokenGeneratorUtil() {

    }

    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenGeneratorUtil.class);

    private static final String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /**
     *
     * @param username
     *            userName
     * @param password
     *            password
     * @return Basic Authentication token
     */
    public static String getUserToken(String username, String password) {
        final String token = username + ":" + password;
        final String encodedBytes = Base64.encodeBase64String(token.getBytes());
        final String encodedToken = "Basic " + encodedBytes;
        return encodedToken;
    }


    public static String alphaNumericGenerator(int limit) {
        final StringBuilder generatedKey = new StringBuilder();
        final Random random = new Random();

        random.nextInt(36);
        for (int i = 0; i < limit; i++) {
            generatedKey.append(key.charAt(random.nextInt(36)));
        }
        return generatedKey.toString();
    }



    public static String getRandomCode() {
        return String.valueOf(getVerificationCode(100001, 999999));
    }

    public static int getVerificationCode(int min, int max) {
        final Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

}
