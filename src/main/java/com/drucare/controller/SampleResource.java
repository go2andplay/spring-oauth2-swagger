package com.drucare.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

/**
 *
 * @author ThirupathiReddy V
 *
 */
@Api(tags = { "Sample" }, description = "Sample Endpoint")
@RestController
@RequestMapping(value="/sample", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleResource {


    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleResource.class);



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Fetches the language file of a given or the default locale",
    authorizations = {@Authorization(value = com.drucare.config.SwaggerConfig.securitySchemaOAuth2,  scopes =
{@AuthorizationScope( scope = "global", description ="des")})})

    public UserDetails login(@RequestBody UserDetails user, HttpServletResponse response) throws Exception {

        LOGGER.info("Registering Original End Manufacturer (OEM) ");

        response.setStatus(201);
        return null;

    }
}
