package com.example.spring.parse.config;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.any;

import com.google.common.collect.Lists;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger documentation config - to visualize and interact with the API’s resources without having
 * any of the implementation logic in place. It’s automatically generated from OpenAPI (formerly
 * known as Swagger) Specification, with the visual documentation making it easy for back end
 * implementation and client side consumption.
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@Component
@Slf4j
public class SwaggerConfig implements WebMvcConfigurer {

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String TOKEN_TYPE = "JWT";

  public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

  /**
   * Main general config
   *
   * @return configured SpringFox Swagger Docket
   */
  @Bean
  public Docket swaggerSpringfoxDocket() {
    log.debug("Starting Swagger");
    StopWatch watch = new StopWatch("Swagger");
    watch.start();
    // set API information
    Contact contact = new Contact(
        "Example Service",
        "http://example-project.com",
        "example_app_service@gmail.com");

    List<VendorExtension> vext = new ArrayList<>();
    ApiInfo apiInfo = new ApiInfo(
        "Example application Backend API",
        "Example application project API reference for developers",
        "0.0.1",
        "http://example_app.com",
        contact,
        "MIT",
        "http://example_app.com",
        vext);

    Docket docket = new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo)
        .pathMapping("/")
        .apiInfo(apiInfo)
        .forCodeGeneration(true)
        .genericModelSubstitutes(ResponseEntity.class)
        .ignoredParameterTypes(Pageable.class)
        .ignoredParameterTypes(java.sql.Date.class)
        .directModelSubstitute(java.time.LocalDate.class, String.class)
        .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
        .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(Lists.newArrayList(apiKey()))
        .useDefaultResponseMessages(false);

    docket = docket.select()
        .apis(any())
        .paths(regex(DEFAULT_INCLUDE_PATTERN))
        .build();
    watch.stop();
    log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
    return docket;
  }

  private ApiKey apiKey() {
    return new ApiKey(TOKEN_TYPE, AUTHORIZATION_HEADER, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
        .build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope
        = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(
        new SecurityReference(TOKEN_TYPE, authorizationScopes));
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
