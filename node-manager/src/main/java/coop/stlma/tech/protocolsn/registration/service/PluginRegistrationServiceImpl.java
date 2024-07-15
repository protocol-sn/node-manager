package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.health.model.HealthResponse;
import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import coop.stlma.tech.protocolsn.registration.data.PluginRegistrationRepository;
import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.HttpClient;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link PluginRegistrationService}
 *
 * @author John Meyerin
 */
@Singleton
@Slf4j
public class PluginRegistrationServiceImpl implements PluginRegistrationService {

    private final PluginRegistrationRepository pluginRegistrationRepository;
    private final ApplicationContext applicationContext;

    public PluginRegistrationServiceImpl(PluginRegistrationRepository pluginRegistrationRepository,
                                         ApplicationContext applicationContext) {
        this.pluginRegistrationRepository = pluginRegistrationRepository;
        this.applicationContext = applicationContext;
    }

    /**
     * Register a plugin by saving it to a database.
     * @param pluginRegistration info on the plugin to register
     * @return the registered plugin
     */
    @Override
    public Mono<PluginRegistration> registerPlugin(PluginRegistration pluginRegistration) {
        log.debug("Registering plugin {} at location {}", pluginRegistration.getPluginName(), pluginRegistration.getPluginLocation());
//        applicationContext.createBean(HttpClient.class, pluginRegistration.getPluginLocation());
        return pluginRegistrationRepository.save(mapToEntity(pluginRegistration))
                .map(this::mapToDomain);
    }

    /**
     * List all registered plugins
     * @return flux of registered plugins
     */
    @Override
    public Flux<PluginRegistration> getRegisteredPlugins() {
        return pluginRegistrationRepository.findAll()
                .map(this::mapToDomain);
    }

    /**
     * Update a plugin's health status
     * @param pluginId      ID of the plugin to update
     * @param newStatus     new health status
     * @return              updated plugin
     */
    @Override
    public Publisher<PluginRegistration> updateHealthStatus(UUID pluginId, HealthResponse newStatus) {
        return pluginRegistrationRepository.findById(pluginId)
                .flatMap(pluginRegistrationEntity -> {
                    String lastHealthCheckStatus = pluginRegistrationEntity.getLastHealthCheckStatus();
                    pluginRegistrationEntity.setCurrentHealthStatus(newStatus.getHealthStatus().name());
                    pluginRegistrationEntity.setCurrentHealthDescription(newStatus.getDescription());
                    pluginRegistrationEntity.setLastHealthCheck(Instant.now());
                    pluginRegistrationEntity.setLastHealthCheckStatus(lastHealthCheckStatus);
                    return pluginRegistrationRepository.update(pluginRegistrationEntity);
                })
                .map(this::mapToDomain);
    }

    @Override
    public Mono<PluginRegistration> getPluginById(UUID pluginId) {
        return pluginRegistrationRepository.findById(pluginId)
                .map(this::mapToDomain);
    }

    private PluginRegistration mapToDomain(PluginRegistrationEntity pluginRegistrationEntity) {
        return new PluginRegistration(pluginRegistrationEntity.getId(), pluginRegistrationEntity.getPluginName(),
                pluginRegistrationEntity.getPluginLocation(), pluginRegistrationEntity.getHealthEndpoint(),
                StringUtils.isNotEmpty(pluginRegistrationEntity.getCurrentHealthStatus()) ? HealthStatus.valueOf(pluginRegistrationEntity.getCurrentHealthStatus()) : null,
                pluginRegistrationEntity.getCurrentHealthDescription(), pluginRegistrationEntity.getLastHealthCheck(),
                StringUtils.isNotEmpty(pluginRegistrationEntity.getLastHealthCheckStatus()) ? HealthStatus.valueOf(pluginRegistrationEntity.getLastHealthCheckStatus()) : null);
    }

    private PluginRegistrationEntity mapToEntity(PluginRegistration pluginRegistration) {
        return new PluginRegistrationEntity(null, pluginRegistration.getPluginName(),
                pluginRegistration.getPluginLocation(), pluginRegistration.getHealthEndpoint(),
                Optional.ofNullable(pluginRegistration.getCurrentHealthStatus()).map(HealthStatus::name).orElse(null),
                pluginRegistration.getCurrentHealthDescription(), pluginRegistration.getLastHealthCheck(),
                Optional.ofNullable(pluginRegistration.getLastHealthCheckStatus()).map(HealthStatus::name).orElse(null));
    }
}
