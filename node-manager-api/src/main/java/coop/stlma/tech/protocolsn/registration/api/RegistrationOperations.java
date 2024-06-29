package coop.stlma.tech.protocolsn.registration.api;

import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import org.reactivestreams.Publisher;

/**
 * Operations for registration of plugins
 *
 * @author John Meyerin
 */

public interface RegistrationOperations {

    String REGISTER_PLUGIN_ENDPOINT = "/plugin-registration";
    String REGISTER_PLUGIN_ROLES = "plugin";
    Publisher<HttpResponse<PluginRegistration>> registerPlugin(@Body PluginRegistration pluginRegistration);
}
