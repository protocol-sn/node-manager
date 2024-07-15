package coop.stlma.tech.protocolsn.health.service;

import coop.stlma.tech.protocolsn.health.model.HealthResponse;
import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import coop.stlma.tech.protocolsn.registration.service.PluginRegistrationService;

import io.micronaut.http.client.HttpClient;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.UUID;

/**
 * Default implementation of the {@link PluginHealthService}
 *
 * @author John Meyerin
 */
@Singleton
@Slf4j
public class PluginHealthServiceImpl implements PluginHealthService {

    private final HttpClient healthClient;
    private final PluginRegistrationService pluginRegistrationService;

    public PluginHealthServiceImpl(HttpClient healthClient,
                                   PluginRegistrationService pluginRegistrationService) {
        this.healthClient = healthClient;
        this.pluginRegistrationService = pluginRegistrationService;
    }

    @Override
    public Mono<Tuple2<UUID, HealthResponse>> getHealthResponse(UUID pluginId) {
        return pluginRegistrationService.getPluginById(pluginId)
                .flatMap(pluginRegistration -> getHealthResponse(pluginRegistration.getPluginName(),
                        pluginRegistration.getPluginLocation() + pluginRegistration.getHealthEndpoint()))
                .map(healthResponse -> Tuples.of(pluginId, healthResponse));
    }

    @Override
    public Flux<Tuple2<UUID, HealthResponse>> healthCheckAllPlugins() {
        return pluginRegistrationService
            .getRegisteredPlugins()
            .flatMap(pluginRegistration ->
                getHealthResponse(pluginRegistration.getId()));
    }

    private Mono<HealthResponse> getHealthResponse(String pluginName, String location) {
        log.trace("Sending request to {}", location);
        log.debug("Running health check for plugin: {}", pluginName);
        return Mono.from(healthClient.exchange(location, HealthResponse.class))
                .map(healthResponseHttpResponse -> {
                    log.debug("Plugin {} health check successful", pluginName);
                    return healthResponseHttpResponse.body();
                })
                .doOnError(throwable -> log.error("Plugin {} health check failed with error {}", pluginName, throwable.getMessage()))
                .onErrorReturn(new HealthResponse(HealthStatus.DOWN, "Plugin " + pluginName + " health check failed with error"));
    }
}
