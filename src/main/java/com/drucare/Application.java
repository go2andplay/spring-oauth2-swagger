package com.drucare;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 *
 * Entry point to the spring boot application
 *
 * @author ThirupathiReddy V
 *
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Override
    public WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        /** This is to check which external Tomcat server suitable */
        LOGGER.info("VirtualServerName {} ", servletContext.getVirtualServerName());
        return super.createRootApplicationContext(servletContext);
    }

    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        System.setProperty("spring.profiles.active", "dev");
        LOGGER.info("Running Spring boot application with profile(external server) :  {}", System.getProperty("spring.profiles.active"));

        return builder.sources(Application.class);
    }

    public static void main(String args[]) {
        System.setProperty("spring.profiles.active", "dev");

        LOGGER.info("Running Spring boot application with profile :  {}", System.getProperty("spring.profiles.active"));

        final SpringApplication application = new SpringApplication(Application.class);
        final Properties properties = new Properties();
        properties.put("spring.thymeleaf.enabled", "true");
        // properties.put("server.servletPath", "/*");// dispatch-servlet path can be set here
        application.setBannerMode(Mode.CONSOLE);
        application.setDefaultProperties(properties);
        application.run();
    }

}

