package coop.stlma.tech.protocolsn.nodemanager.registration.data;

import coop.stlma.tech.protocolsn.nodemanager.registration.data.entity.PluginRegistrationEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * CrUD repository for registered plugins
 *
 * @author John Meyerin
 */
@Repository
public interface PluginRegistrationRepository extends ReactorCrudRepository<PluginRegistrationEntity, UUID> {

    /**
     * Find a registered plugin by its name
     * @param pluginName    name of the plugin
     * @return              plugin
     */
    Mono<PluginRegistrationEntity> findByPluginName(String pluginName);
}