package com.drucare.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.drucare.interceptor.RESTInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author ThirupathiReddy V
 *
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class WebConfig extends WebMvcConfigurerAdapter {

    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addRedirectViewController("/", "/swagger-ui.html");
        //registry.addViewController("/api-console").setViewName("index");

    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath:messages/messages");
        messageBundle.setUseCodeAsDefaultMessage(true);
        messageBundle.setDefaultEncoding("UTF-8");
        return messageBundle;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        LOGGER.info("Enabling Cross Origin Resource Sharing");
        registry.addMapping("/**").allowedMethods("OPTIONS", "DELETE", "POST", "PUT", "GET");
        // registry.addMapping("/users/**").allowedMethods("OPTIONS", "DELETE", "POST", "PUT", "GET");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/drucare/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
    }

    @Bean
    public StringHttpMessageConverter stringConverter() {
        final StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
        return stringConverter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jaksonMessageConverter() {
        final MappingJackson2HttpMessageConverter jaksonMessageConverter = new MappingJackson2HttpMessageConverter();
        jaksonMessageConverter.setObjectMapper(objectMapper());
        return jaksonMessageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jaksonMessageConverter());
        converters.add(stringConverter());
        super.configureMessageConverters(converters);
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        // final DateFormat dateFormat = new SimpleDateFormat(DateUtil.FORMAT);
        // dateFormat.setTimeZone(TimeZone.getDefault());
        // objectMapper.setDateFormat(dateFormat);

        return objectMapper;
    }

    // @Bean
    public FilterRegistrationBean jwtFilter() {
        LOGGER.info("Preparing filter mapping for the authentication ");

        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        // registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/users/*");

        return registrationBean;
    }

    public List<ClientHttpRequestInterceptor> interceptors() {
        final List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
        ris.add(new RESTInterceptor());
        return ris;
    }

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        final SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(3000);
        simpleClientHttpRequestFactory.setConnectTimeout(3000);
        simpleClientHttpRequestFactory.setBufferRequestBody(true);
        simpleClientHttpRequestFactory.setOutputStreaming(true);
        return simpleClientHttpRequestFactory;
    }

    @Bean
    public BufferingClientHttpRequestFactory requestFactory() {
        final BufferingClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory());
        return factory;
    }

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors());

        restTemplate.setRequestFactory(requestFactory());

        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(objectMapper());
        messageConverters.add(jsonMessageConverter);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            private static final String CONTEXT_PATH = "/drucare-api";

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                container.setSessionTimeout(30, TimeUnit.MINUTES);
                int port = 8084;
                try {
                    final String portString = System.getProperty("server.port");
                    if(portString!=null) {
                        port = Integer.parseInt(portString);
                    }

                    LOGGER.info("Server running on the port {} ", port);
                } catch (final NumberFormatException e) {
                    LOGGER.debug("Error while reading server.port ",e);
                }
                LOGGER.trace("Customizing embeddedServlet container  using port :{} and contextPath :{}", port, CONTEXT_PATH);
                container.setPort(port);// on which port embedded tomcat should
                // container.setContextPath(CONTEXT_PATH);// This is to make
                // in sync with direct TOMCAT deployment and embedded server
                // deployment
            }
        };
    }

}
