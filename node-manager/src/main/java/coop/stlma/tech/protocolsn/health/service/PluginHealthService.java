package coop.stlma.tech.protocolsn.health.service;

import coop.stlma.tech.protocolsn.health.model.HealthResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.UUID;

/**
 * A service that checks the health of registered plugins
 *
 * @author John Meyerin
 */
public interface PluginHealthService {

    /**
     * Get the health response for a single plugin
     * @param pluginId  ID of the plugin
     * @return          health of the given plugin
     */
    Mono<Tuple2<UUID, HealthResponse>> getHealthResponse(UUID pluginId);

    /**
     * Get the health responses of all registered plugins
     * @return  health responses of all registered plugins
     */
    Flux<Tuple2<UUID, HealthResponse>> healthCheckAllPlugins();
}
