package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.registration.domain.PluginRegistration;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Service interface for registering plugins
 *
 * @author John Meyerin
 */
public interface PluginRegistrationService {

    /**
     * Register a plugin
     * @param pluginRegistration info on the plugin to register
     * @return the registered plugin
     */
    Mono<PluginRegistration> registerPlugin(PluginRegistration pluginRegistration);
}
