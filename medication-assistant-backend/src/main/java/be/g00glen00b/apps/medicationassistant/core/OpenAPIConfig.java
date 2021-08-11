package be.g00glen00b.apps.medicationassistant.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public Docket medicationAssistantApi() {
        return new Docket(DocumentationType.OAS_30)
            .groupName("medication-assistant-api")
            .useDefaultResponseMessages(false)
            .apiInfo(medicationAssistantApiInfo())
            .select()
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(List.of(basicAuth()));
    }

    private BasicAuth basicAuth() {
        return new BasicAuth("basicAuth");
    }

    private ApiInfo medicationAssistantApiInfo() {
        return new ApiInfoBuilder().title("Medication Assistant API").build();
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder()
            .enableCsrfSupport(true)
            .build();
    }
}
