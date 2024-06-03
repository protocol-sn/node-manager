package coop.stlma.tech.protocolsn.registration.controller;

import coop.stlma.tech.protocolsn.registration.domain.PluginRegistration;
import coop.stlma.tech.protocolsn.registration.service.PluginRegistrationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PluginRegistrationController {

    private final PluginRegistrationService pluginRegistrationService;

    public PluginRegistrationController(PluginRegistrationService pluginRegistrationService) {
        this.pluginRegistrationService = pluginRegistrationService;
    }

    @Post("/plugin-registration")
    @RolesAllowed("plugin")
    @SecurityRequirement(name = "pluginClient")
    public Publisher<HttpResponse<PluginRegistration>> registerPlugin(@Body PluginRegistration pluginRegistration) {
        return Mono.from(pluginRegistrationService.registerPlugin(pluginRegistration))
                .map(HttpResponse::ok);

    }
}
