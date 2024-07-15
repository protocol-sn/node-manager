package coop.stlma.tech.protocolsn.health.service;

import coop.stlma.tech.protocolsn.health.api.HealthOperations;
import coop.stlma.tech.protocolsn.health.model.HealthResponse;
import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import coop.stlma.tech.protocolsn.registration.service.PluginRegistrationService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Property;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MicronautTest
@Property(name = "activeTest", value = "HealthControllerTest")
class PluginHealthServiceImplTest {

    @Inject
    PluginHealthServiceImpl pluginHealthService;

    @MockBean
    @Primary
    PluginRegistrationService pluginRegistrationService = Mockito.mock(PluginRegistrationService.class);

    @Inject
    EmbeddedServer embeddedServer;

    EmbeddedServer otherServer = ApplicationContext.run(EmbeddedServer.class, Map.of(
            "healthResponse", "unhealthy",
            "activeTest", "HealthControllerTest"));

    @Test
    void testGetHealthResponse_happyPath() {
        UUID requestId = UUID.nameUUIDFromBytes("test".getBytes());

        Mockito.when(pluginRegistrationService.getPluginById(requestId))
                        .thenReturn(Mono.just(new PluginRegistration(requestId, "test",
                                embeddedServer.getURI().toString(), HealthOperations.HEALTH_ENDPOINT, null,
                                null, null, null)));

        Tuple2<UUID, HealthResponse> healthResponse = pluginHealthService.getHealthResponse(requestId).block();

        Assertions.assertNotNull(healthResponse);
        Assertions.assertEquals(requestId, healthResponse.getT1());
        Assertions.assertEquals(HealthStatus.HEALTHY, healthResponse.getT2().getHealthStatus());
        Assertions.assertEquals("I am healthy", healthResponse.getT2().getDescription());
    }

    @Test
    void testHealthCheckAllPlugins_happyPath() {
        UUID plugin1 = UUID.nameUUIDFromBytes("one".getBytes());
        UUID plugin2 = UUID.nameUUIDFromBytes("two".getBytes());
        UUID plugin3 = UUID.nameUUIDFromBytes("three".getBytes());

        List<PluginRegistration> expectedPlugins = List.of(
                new PluginRegistration(plugin1, "one",
                        embeddedServer.getURI().toString(), HealthOperations.HEALTH_ENDPOINT, null,
                        null, null, null),
                new PluginRegistration(plugin2, "two",
                        otherServer.getURI().toString(), HealthOperations.HEALTH_ENDPOINT, null,
                        null, null, null),
                new PluginRegistration(plugin3, "three",
                        "http://localhost:8090", HealthOperations.HEALTH_ENDPOINT, null,
                        null, null, null));

        Mockito.when(pluginRegistrationService.getRegisteredPlugins())
                .thenReturn(Flux.fromIterable(expectedPlugins));

        Mockito.when(pluginRegistrationService.getPluginById(plugin1))
                .thenReturn(Mono.just(expectedPlugins.get(0)));
        Mockito.when(pluginRegistrationService.getPluginById(plugin2))
                .thenReturn(Mono.just(expectedPlugins.get(1)));
        Mockito.when(pluginRegistrationService.getPluginById(plugin3))
                .thenReturn(Mono.just(expectedPlugins.get(2)));

        List<Tuple2<UUID, HealthResponse>> responses = pluginHealthService.healthCheckAllPlugins().collectList().block()
                .stream().sorted(Comparator.comparing(Tuple2::getT1)).toList();

        Assertions.assertEquals(expectedPlugins.size(), responses.size());

        Assertions.assertEquals(plugin1, responses.get(1).getT1());
        Assertions.assertEquals(HealthStatus.HEALTHY, responses.get(1).getT2().getHealthStatus());
        Assertions.assertEquals("I am healthy", responses.get(1).getT2().getDescription());

        Assertions.assertEquals(plugin2, responses.get(0).getT1());
        Assertions.assertEquals(HealthStatus.UP_WITH_PROBLEMS, responses.get(0).getT2().getHealthStatus());
        Assertions.assertEquals("cough cough", responses.get(0).getT2().getDescription());

        Assertions.assertEquals(plugin3, responses.get(2).getT1());
        Assertions.assertEquals(HealthStatus.DOWN, responses.get(2).getT2().getHealthStatus());
        Assertions.assertEquals("Plugin three health check failed with error", responses.get(2).getT2().getDescription());
    }

    @BeforeEach
    void setup() {
        if (!otherServer.isRunning()) {
            otherServer.start();
        }
    }

    @AfterEach
    void tearDown() {
        if (otherServer.isRunning()) {
            otherServer.stop();
        }
    }
}
