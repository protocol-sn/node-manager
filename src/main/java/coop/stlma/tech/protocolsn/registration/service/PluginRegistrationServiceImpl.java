package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.registration.data.PluginRegistrationRepository;
import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.registration.domain.PluginRegistration;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class PluginRegistrationServiceImpl implements PluginRegistrationService {

    private final PluginRegistrationRepository pluginRegistrationRepository;

    public PluginRegistrationServiceImpl(PluginRegistrationRepository pluginRegistrationRepository) {
        this.pluginRegistrationRepository = pluginRegistrationRepository;
    }

    @Override
    public Mono<PluginRegistration> registerPlugin(PluginRegistration pluginRegistration) {
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
