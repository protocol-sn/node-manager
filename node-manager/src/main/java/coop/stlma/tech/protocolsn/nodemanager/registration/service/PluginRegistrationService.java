package coop.stlma.tech.protocolsn.nodemanager.registration.service;

import coop.stlma.tech.protocolsn.pluginlib.HealthResponse;
import coop.stlma.tech.protocolsn.nodemanager.registration.model.PluginRegistration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

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

    Flux<PluginRegistration> getRegisteredPlugins();

    /**
     * Update the health status of the plugin
     * @param pluginId      ID of the plugin to update
     * @param newStatus     new health status
     * @return the updated plugin
     */
    Mono<PluginRegistration> updateHealthStatus(UUID pluginId, HealthResponse newStatus);

    /**
     * Get a single registered plugin
     * @param pluginId      ID of the plugin
     * @return the plugin
     */
    Mono<PluginRegistration> getPluginById(UUID pluginId);
}
