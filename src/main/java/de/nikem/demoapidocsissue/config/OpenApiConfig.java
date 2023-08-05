package de.nikem.demoapidocsissue.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;

import java.util.Optional;

@Configuration
public class OpenApiConfig {

    /**
     * API specification: .../v3/api-docs.yaml/api
     */
    @Bean
    public GroupedOpenApi api() {
        return createDefaultGroupedOpenApi("api", "/api/**");
    }

    @Bean
    public GroupedOpenApi all() {
        return createDefaultGroupedOpenApi("", "/**");
    }

    private GroupedOpenApi createDefaultGroupedOpenApi(String groupName, String pathsToMatch) {
        return GroupedOpenApi.builder()
                .group(groupName)
                .pathsToMatch(pathsToMatch)
                .addOperationCustomizer(new OpenApiPreAuthorizeCustomizer())
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String apiTitle = "Demo REST API";
        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(
                        new Components()
                                .addSecuritySchemes("JWT",
                                        new SecurityScheme()
                                                .name("X-Auth")
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .description("JWT Token"))
                )
                .info(new Info().title(apiTitle));
    }


    private static class OpenApiPreAuthorizeCustomizer implements OperationCustomizer {

        @Override
        public Operation customize(Operation operation, HandlerMethod handlerMethod) {
            PreAuthorize preAuthorizeAnnotation = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            getAccessControlExpression(preAuthorizeAnnotation).ifPresent(ace -> {
                String currentDescription = operation.getDescription();
                String newDescription = StringUtils.join(currentDescription, System.lineSeparator(), ace);
                operation.setDescription(newDescription);
            });

            return operation;
        }

        private Optional<String> getAccessControlExpression(PreAuthorize preAuthorizeAnnotation) {
            if (preAuthorizeAnnotation == null) {
                return Optional.empty();
            }

            return Optional.of(String.format("**Security @PreAuthorize expression:** `%s`", preAuthorizeAnnotation.value()));
        }
    }
}
