package coop.stlma.tech.protocolsn.nodemanager.health.service;

import coop.stlma.tech.protocolsn.health.api.HealthClient;
import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import coop.stlma.tech.protocolsn.pluginlib.HealthResponse;
import coop.stlma.tech.protocolsn.nodemanager.registration.service.PluginRegistrationService;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * Default implementation of the {@link PluginHealthService}
 *
 * @author John Meyerin
 */
@Singleton
@Slf4j
public class PluginHealthServiceImpl implements PluginHealthService {

    private final PluginRegistrationService pluginRegistrationService;
    private final Executor executor;

    public PluginHealthServiceImpl(PluginRegistrationService pluginRegistrationService,
                                   @Named(TaskExecutors.IO) Executor executor) {
        this.pluginRegistrationService = pluginRegistrationService;
        this.executor = executor;
    }

    /**
     * Get the health response for a single plugin
     * @param pluginId  ID of the plugin
     * @return          health of the given plugin
     */
    @Override
    public Mono<Tuple2<UUID, HealthResponse>> getHealthResponse(UUID pluginId) {
        return pluginRegistrationService.getPluginById(pluginId)
                .flatMap(pluginRegistration -> getHealthResponse(pluginRegistration.getPluginName(),
                        pluginRegistration.getPluginLocation(),
                        pluginRegistration.getPluginGrpcPort(),
                        executor))
                .map(healthResponse -> Tuples.of(pluginId, healthResponse));
    }

    /**
     * Get the health responses of all registered plugins
     * @return  health responses of all registered plugins
     */
    @Override
    public Flux<Tuple2<UUID, HealthResponse>> healthCheckAllPlugins() {
        return pluginRegistrationService
            .getRegisteredPlugins()
            .flatMap(pluginRegistration ->
                getHealthResponse(pluginRegistration.getId()));
    }

    private Mono<HealthResponse> getHealthResponse(String pluginName, String location, int grpcPort, Executor executor) {
        log.trace("Sending request to {}", location);
        log.debug("Running health check for plugin: {}", pluginName);
        HealthClient myClient = HealthClient.create(location, grpcPort, executor);
        return Mono.from(myClient.healthCheck())
                .doOnError(throwable -> {
                    log.error("Plugin {} health check failed with error {}", pluginName, throwable.getMessage());
                    throwable.printStackTrace();
                })
                .onErrorReturn(HealthResponse.newBuilder()
                        .setHealthStatus(HealthStatus.DOWN.name())
                        .setDescription("Plugin " + pluginName + " health check failed with error")
                        .build())
                .doFinally(signalType -> myClient.closeChannel());
    }
}
