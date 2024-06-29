package coop.stlma.tech.protocolsn.registration.controller;

import coop.stlma.tech.protocolsn.registration.api.RegistrationOperations;
import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import coop.stlma.tech.protocolsn.registration.service.PluginRegistrationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Controller for registration of plugins
 *
 * @author John Meyerin
 */
@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
@Slf4j
public class PluginRegistrationController implements RegistrationOperations {

    private final PluginRegistrationService pluginRegistrationService;

    public PluginRegistrationController(PluginRegistrationService pluginRegistrationService) {
        this.pluginRegistrationService = pluginRegistrationService;
    }

    @Post(REGISTER_PLUGIN_ENDPOINT)
    @RolesAllowed(REGISTER_PLUGIN_ROLES)
    @SecurityRequirement(name = "pluginClient")
    @Override
    public Publisher<HttpResponse<PluginRegistration>> registerPlugin(@Body PluginRegistration pluginRegistration) {
        log.debug("registering plugin {}", pluginRegistration.getPluginName());
        return Mono.from(pluginRegistrationService.registerPlugin(pluginRegistration))
                .map(HttpResponse::ok);

    }
}
