package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.registration.data.PluginRegistrationRepository;
import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
        log.debug("Registering plugin {}", pluginRegistration.getPluginName());
        return pluginRegistrationRepository.save(mapToEntity(pluginRegistration))
                .map(this::mapToDomain);
    }

    private PluginRegistration mapToDomain(PluginRegistrationEntity pluginRegistrationEntity) {
        return new PluginRegistration(pluginRegistrationEntity.getId(), pluginRegistrationEntity.getPluginName(), pluginRegistrationEntity.getPluginLocation());
    }

    private PluginRegistrationEntity mapToEntity(PluginRegistration pluginRegistration) {
        return new PluginRegistrationEntity(null, pluginRegistration.getPluginName(), pluginRegistration.getPluginLocation());
    }
}
