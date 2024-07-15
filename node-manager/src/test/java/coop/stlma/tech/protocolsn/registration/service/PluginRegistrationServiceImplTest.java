package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.registration.data.PluginRegistrationRepository;
import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import io.micronaut.context.annotation.Primary;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.UUID;

@MicronautTest
class PluginRegistrationServiceImplTest {

    @Inject
    PluginRegistrationService pluginRegistrationService;

    @MockBean
    @Primary
    PluginRegistrationRepository pluginRegistrationRepository = Mockito.mock(PluginRegistrationRepository.class);

    @Test
    void testRegisterPlugin_happyPath() {
        Mockito.when(pluginRegistrationRepository.save(Mockito.any(PluginRegistrationEntity.class)))
                .thenReturn(Mono.just(new PluginRegistrationEntity(UUID.nameUUIDFromBytes("blog-plugin".getBytes()),
                        "blog-plugin", "localhost:8082", null, null,
                        null, null, null)));
        pluginRegistrationService.registerPlugin(new PluginRegistration(null, "blog-plugin",
                "localhost:8082", null, null, null,
                null, null)).block();
    }
}
