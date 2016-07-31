package com.drucare.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import com.drucare.controller.SampleResource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ImplicitGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author ThirupathiReddy V
 *         <ul>
 *         <li>https://springfox.github.io/springfox/docs/current/</li>
 *         <li>/api/v2/api-docs</li>
 *         <li>/api-console</li>
 *         </ul>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerConfig.class);

    @Autowired
    private MessageSource msgSource;

    //@formatter:off
    @Bean
    public Docket wyzbeeApi() {
        LOGGER.info("Creating Docket for drucare api");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("drucare-api")
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(SampleResource.class.getPackage().getName()))
                //.paths(RequestHandlerSelectors.any())
                .build()
                .securitySchemes(newArrayList(securitySchema()))
                .securityContexts(newArrayList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // .title("Wyzbee MicroServices API")
                .description(msgSource.getMessage("drucare.api.description", null, LocaleContextHolder.getLocale()))
                .termsOfServiceUrl("http://dru.care")
                // .contact("Redpine Signals").license("Apache License Version 2.0")
                // .licenseUrl("https://redpinesignals.com/LICENSE")
                .version("1.0").build();
    }



    @Bean
    public SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*.*"))
                .build();
    }



    List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope
        = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("mykey", authorizationScopes));
    }


    @Bean
    SecurityScheme apiKey() {
        return new ApiKey("Authentication Token", "Authorization", "header");// header ,query two values allowed
    }

    public static final String securitySchemaOAuth2 = "oauth2schema";

    private OAuth securitySchema() {
        return new OAuth(securitySchemaOAuth2, newArrayList(scopes()), newArrayList(grantTypes()));
    }



    @Bean
    SecurityScheme oauths() {
        return new OAuthBuilder().name("drucare_auth").grantTypes(grantTypes()).scopes(scopes()).build();
    }


    List<AuthorizationScope> scopes() {
        return newArrayList(new AuthorizationScope("write:*", "modify endpoints"), new AuthorizationScope("read:*", "read endpoints"));
    }



    List<GrantType> grantTypes() {
        final GrantType grantType = new ImplicitGrantBuilder().loginEndpoint(new LoginEndpoint("http://localhost:8084/oauth/token")).build();
        return newArrayList(grantType);
    }

    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration("clientapp", "123456", "drucare-realm", "drucare-microservices-api", "AuthenticationToken", ApiKeyVehicle.HEADER,
                ",");
    }

    //@formatter:on

    /*
   @formatter:off


    @Bean
    public Docket userApi() {
        final AuthorizationScope[] authScopes = new AuthorizationScope[1];
        authScopes[0] = new AuthorizationScopeBuilder().scope("read").description("read access").build();
        final SecurityReference securityReference = SecurityReference.builder().reference("test").scopes(authScopes).build();

        final ArrayList<SecurityContext> securityContexts = newArrayList(SecurityContext.builder().securityReferences(newArrayList(securityReference)).build());
        return new Docket(DocumentationType.SWAGGER_2).securitySchemes(newArrayList(new BasicAuth("test"))).securityContexts(securityContexts)
                .groupName("user-api").apiInfo(apiInfo()).select().paths(userOnlyEndpoints()).build();
    }

 @Bean
    SecurityContext securityContext() {
        final AuthorizationScope readScope = new AuthorizationScope("read:*", "read all the endpoints");
        final AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = readScope;
        final SecurityReference securityReference = SecurityReference.builder().reference("drucare_auth").scopes(scopes).build();

        return SecurityContext.builder().securityReferences(newArrayList(securityReference)).forPaths(userOnlyEndpoints()).build();
    }


 private Predicate<String> userOnlyEndpoints() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                System.err.println(input);
                return false;
            }
        };
    }



    @formatter:on
     */
}