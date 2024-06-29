package coop.stlma.tech.protocolsn.registration.controller;

import coop.stlma.tech.protocolsn.registration.model.PluginRegistration;
import coop.stlma.tech.protocolsn.registration.service.PluginRegistrationService;
import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@MicronautTest
class PluginRegistrationControllerTest {

    @MockBean
    @Primary
    PluginRegistrationService pluginRegistrationService = Mockito.mock(PluginRegistrationService.class);

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void testRegisterPlugin_happyPath() {
        PluginRegistration expected = new PluginRegistration(UUID.nameUUIDFromBytes("blog-plugin".getBytes()), "blog-plugin", "localhost:8082");
        Mockito.when(pluginRegistrationService.registerPlugin(Mockito.any(PluginRegistration.class)))
                .thenReturn(Mono.just(expected));

        HttpResponse<PluginRegistration> response = httpClient.toBlocking()
                .exchange(HttpRequest
                        .POST("/plugin-registration",
                                Map.of("pluginName", "blog-plugin",
                                       "pluginLocation", "localhost:8082")));

        PluginRegistration pluginRegistration = response.getBody(PluginRegistration.class).get();
        Assertions.assertNotNull(pluginRegistration);
        Assertions.assertEquals("blog-plugin", pluginRegistration.getPluginName());
        Assertions.assertEquals("localhost:8082", pluginRegistration.getPluginLocation());
    }
}
