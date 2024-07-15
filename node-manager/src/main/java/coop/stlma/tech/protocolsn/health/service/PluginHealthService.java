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

    Mono<Tuple2<UUID, HealthResponse>> getHealthResponse(UUID pluginId);

    Flux<Tuple2<UUID, HealthResponse>> healthCheckAllPlugins();
}
