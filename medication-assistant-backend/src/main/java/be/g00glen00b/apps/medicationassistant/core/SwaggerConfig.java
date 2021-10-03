package be.g00glen00b.apps.medicationassistant.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket medicationAssistantApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .securityContexts(List.of(securityContext()))
            .securitySchemes(List.of(basicAuth()))
            .groupName("medication-assistant-api")
            .useDefaultResponseMessages(false)
            .apiInfo(medicationAssistantApiInfo())
            .ignoredParameterTypes(AuthenticationPrincipal.class)
            .directModelSubstitute(Pageable.class, SwaggerPageable.class)
            .select()
                .paths(PathSelectors.any())
            .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(List.of(defaultAuth()))
            .operationSelector(context -> true)
            .build();
    }

    private SecurityReference defaultAuth() {
        return SecurityReference.builder()
            .reference("basicAuth")
            .scopes(new AuthorizationScope[0])
            .build();
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
            .build();
    }

    @Getter
    @Setter
    private static class SwaggerPageable {
        @ApiModelProperty(value = "Page you want to retrieve (zero-based)", example = "0")
        private int page;
        @ApiModelProperty(value = "Amount of records per page", example = "20")
        private int size;
        @ApiModelProperty("Sorting criteria formatted as property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
        private String sort;
    }
}
