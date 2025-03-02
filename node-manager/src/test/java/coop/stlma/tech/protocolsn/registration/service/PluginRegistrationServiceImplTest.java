package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.health.model.HealthResponse;
import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import coop.stlma.tech.protocolsn.registration.data.PluginRegistrationRepository;
import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import io.micronaut.context.annotation.Primary;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.UUID;

@MicronautTest
class PluginRegistrationServiceImplTest {

    public static final UUID PLUGIN_ID = UUID.nameUUIDFromBytes("blog-plugin".getBytes());
    @Inject
    PluginRegistrationService pluginRegistrationService;

    @MockBean
    @Primary
    PluginRegistrationRepository pluginRegistrationRepository = Mockito.mock(PluginRegistrationRepository.class);

    ArgumentCaptor<PluginRegistrationEntity> pluginRegistrationEntityArgumentCaptor = ArgumentCaptor.forClass(PluginRegistrationEntity.class);

    @Test
    void testRegisterPlugin_newRegistration() {
        Mockito.when(pluginRegistrationRepository.findByPluginName("blog-plugin"))
                        .thenReturn(Mono.empty());
        Mockito.when(pluginRegistrationRepository.update(Mockito.any(PluginRegistrationEntity.class)))
                .thenReturn(Mono.just(new PluginRegistrationEntity(UUID.nameUUIDFromBytes("blog-plugin".getBytes()),
                        "blog-plugin", "localhost:8082", null, null,
                        null, null, null)));
        PluginRegistration result = pluginRegistrationService.registerPlugin(new PluginRegistration(null, "blog-plugin",
                "localhost:8082", null, null, null,
                null, null)).block();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("blog-plugin", result.getPluginName());
        Assertions.assertEquals("localhost:8082", result.getPluginLocation());
    }

    @Test
    void testRegisterPlugin_reRegistration() {
        Mockito.when(pluginRegistrationRepository.findByPluginName("blog-plugin"))
                .thenReturn(Mono.just(new PluginRegistrationEntity(UUID.nameUUIDFromBytes("blog-plugin".getBytes()),
                        "blog-plugin", "localhost:8082", null, null,
                        null, null, null)));
        Mockito.when(pluginRegistrationRepository.update(pluginRegistrationEntityArgumentCaptor.capture()))
                .thenReturn(Mono.just(new PluginRegistrationEntity(UUID.nameUUIDFromBytes("blog-plugin".getBytes()),
                        "blog-plugin", "localhost:8083", null, null,
                        null, null, null)));
        PluginRegistration result = pluginRegistrationService.registerPlugin(new PluginRegistration(null, "blog-plugin",
                "localhost:8083", null, null, null,
                null, null)).block();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("blog-plugin", result.getPluginName());
        Assertions.assertEquals("localhost:8083", result.getPluginLocation());
        Assertions.assertEquals("localhost:8083", pluginRegistrationEntityArgumentCaptor.getValue().getPluginLocation());
    }

    @Test
    void testUpdateHealthStatus_happyPath() {
        PluginRegistrationEntity expectedEntity = new PluginRegistrationEntity(PLUGIN_ID,
                "blog-plugin", "localhost:8082", null, null,
                null, null, null);
        Mockito.when(pluginRegistrationRepository.findById(PLUGIN_ID))
                .thenReturn(Mono.just(expectedEntity));

        Mockito.when(pluginRegistrationRepository.update(pluginRegistrationEntityArgumentCaptor.capture()))
                .thenReturn(Mono.just(expectedEntity));

        PluginRegistration result = pluginRegistrationService.updateHealthStatus(PLUGIN_ID, new HealthResponse(HealthStatus.HEALTHY, "Happiness")).block();

        Assertions.assertEquals("blog-plugin", result.getPluginName());
        PluginRegistrationEntity pluginRegistrationEntity = pluginRegistrationEntityArgumentCaptor.getValue();
        Assertions.assertEquals(PLUGIN_ID, pluginRegistrationEntity.getId());
        Assertions.assertEquals("localhost:8082", pluginRegistrationEntity.getPluginLocation());
        Assertions.assertEquals("HEALTHY", pluginRegistrationEntity.getCurrentHealthStatus());
        Assertions.assertEquals("Happiness", pluginRegistrationEntity.getCurrentHealthDescription());
    }
}
