package com.drucare.interceptor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * This Interceptor helps to view the payload that is passed to the core services
 *
 * @author ThirupathiReddy V
 *
 */
public class RESTInterceptor implements ClientHttpRequestInterceptor {

    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(" CORE SERVICE >> ");

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final ClientHttpResponse response = execution.execute(request, body);
        log(request, body, response);
        return response;
    }

    private void log(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        LOGGER.info(" [ {} ] => {} ", request.getMethod(), request.getURI());
        if (body != null && body.length > 1) {
            LOGGER.info(" Payload \n {} ", new String(body));
        }

    }
}
