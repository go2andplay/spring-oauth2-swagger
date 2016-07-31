package com.drucare.api.advice;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This controller will sends error messages in the JSON format
 *
 * @author ThirupathiReddy V
 *
 */

@RestController
public class WyzbeeErrorController implements ErrorController {
    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WyzbeeErrorController.class);

    private static final String PATH = "/error";

    private boolean debug;

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public ErrorJson error(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Error occured while accessing wyzbee services ");

        return new ErrorJson(response.getStatus(), getErrorAttributes(request, debug));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
