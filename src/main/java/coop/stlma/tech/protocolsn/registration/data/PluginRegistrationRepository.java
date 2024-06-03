package coop.stlma.tech.protocolsn.registration.data;

import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;

import java.util.UUID;

@Repository
public interface PluginRegistrationRepository extends ReactorCrudRepository<PluginRegistrationEntity, UUID> {
}