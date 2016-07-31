package com.drucare.controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author ThirupathiReddy V
 *
 */
@Api(tags = { "Login" }, description = "Sample Endpoint")
@RestController
@RequestMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginResource {

    @ApiOperation(value = "Creates new OEM user")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void get() throws Exception {



    }
}
