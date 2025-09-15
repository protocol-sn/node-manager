package coop.stlma.tech.protocolsn.nodemanager.registration.service;

import coop.stlma.tech.protocolsn.commonlib.util.ProtoUtil;
import coop.stlma.tech.protocolsn.pluginlib.HealthResponse;
import coop.stlma.tech.protocolsn.nodemanager.registration.data.PluginRegistrationRepository;
import coop.stlma.tech.protocolsn.nodemanager.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.nodemanager.PluginRegistration;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.Instant;
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

    public PluginRegistrationServiceImpl(PluginRegistrationRepository pluginRegistrationRepository) {
        this.pluginRegistrationRepository = pluginRegistrationRepository;
    }

    /**
     * Register a plugin by saving it to a database.
     * @param pluginRegistration info on the plugin to register
     * @return the registered plugin
     */
    @Override
    public Mono<PluginRegistration> registerPlugin(PluginRegistration pluginRegistration) {
        log.debug("Registering plugin {} at location {} and port {}", pluginRegistration.getPluginName(), pluginRegistration.getPluginLocation(), pluginRegistration.getPluginGrpcPort());
        return pluginRegistrationRepository.findByPluginName(pluginRegistration.getPluginName())
                .singleOptional()
                .map(pluginRegistrationEntity -> {
                    PluginRegistrationEntity saveMe = mapToEntity(pluginRegistration);
                    pluginRegistrationEntity.ifPresent(registrationEntity ->
                            saveMe.setId(registrationEntity.getId()));
                    return saveMe;
                })
                .flatMap(pluginRegistrationRepository::update)
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
    public Mono<PluginRegistration> updateHealthStatus(UUID pluginId, HealthResponse newStatus) {
        log.debug("Updating plugin with id {} to have health status {}", pluginId, newStatus.getHealthStatus());
        return pluginRegistrationRepository.findById(pluginId)
                .map(pluginRegistrationEntity -> {
                    String lastHealthCheckStatus = pluginRegistrationEntity.getLastHealthCheckStatus();
                    pluginRegistrationEntity.setCurrentHealthStatus(newStatus.getHealthStatus());
                    pluginRegistrationEntity.setCurrentHealthDescription(newStatus.getDescription());
                    pluginRegistrationEntity.setLastHealthCheck(Instant.now());
                    pluginRegistrationEntity.setLastHealthCheckStatus(lastHealthCheckStatus);
                    return pluginRegistrationEntity;
                })
                .flatMap(pluginRegistrationRepository::update)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<PluginRegistration> getPluginById(UUID pluginId) {
        return pluginRegistrationRepository.findById(pluginId)
                .map(this::mapToDomain);
    }

    private PluginRegistration mapToDomain(PluginRegistrationEntity pluginRegistrationEntity) {
        PluginRegistration.Builder builder = PluginRegistration.newBuilder()
                .setId(pluginRegistrationEntity.getId().toString())
                .setPluginName(pluginRegistrationEntity.getPluginName())
                .setPluginLocation(pluginRegistrationEntity.getPluginLocation())
                .setPluginGrpcPort(pluginRegistrationEntity.getPluginGrpcPort());

        if (pluginRegistrationEntity.getCurrentHealthStatus() != null) {
            builder.setCurrentHealthStatus(pluginRegistrationEntity.getCurrentHealthStatus());
        }
        if (pluginRegistrationEntity.getCurrentHealthDescription() != null) {
            builder.setCurrentHealthDescription(pluginRegistrationEntity.getCurrentHealthDescription());
        }
        if (pluginRegistrationEntity.getLastHealthCheck() != null) {
            builder.setLastHealthCheck(ProtoUtil.fromInstant(pluginRegistrationEntity.getLastHealthCheck()));
        }
        if (pluginRegistrationEntity.getLastTimeHealthy() != null) {
            builder.setLastTimeHealthy(ProtoUtil.fromInstant(pluginRegistrationEntity.getLastTimeHealthy()));
        }
        return builder.build();
    }

    private PluginRegistrationEntity mapToEntity(PluginRegistration pluginRegistration) {
        log.debug("Mapping plugin {} at location {}", pluginRegistration.getPluginName(), pluginRegistration.getPluginLocation());
        return new PluginRegistrationEntity(
                null,
                pluginRegistration.getPluginName(),
                pluginRegistration.getPluginLocation(),
                pluginRegistration.getPluginGrpcPort(),
                pluginRegistration.getPluginTarget(),
                pluginRegistration.getCurrentHealthStatus(),
                pluginRegistration.getCurrentHealthDescription(),
                ProtoUtil.fromTimestamp(pluginRegistration.getLastHealthCheck()),
                pluginRegistration.getLastHealthCheckStatus(),
                ProtoUtil.fromTimestamp(pluginRegistration.getLastTimeHealthy()));
    }
}
