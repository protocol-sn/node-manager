package coop.stlma.tech.protocolsn.registration.api;

import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import jakarta.annotation.security.RolesAllowed;
import org.reactivestreams.Publisher;

/**
 * Client for registration of plugins
 *
 * @author John Meyerin
 */
@Client("node-manager::registration")
public interface RegistrationClient extends RegistrationOperations {

    /**
     * Register the plugin with the node manager
     * @param pluginRegistration    info on the plugin
     * @return                      the registered plugin
     */
    @Post(REGISTER_PLUGIN_ENDPOINT)
    @RolesAllowed(REGISTER_PLUGIN_ROLES)
    Publisher<HttpResponse<PluginRegistration>> registerPlugin(@Body PluginRegistration pluginRegistration);
}
