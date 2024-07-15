package coop.stlma.tech.protocolsn.registration.data;

import coop.stlma.tech.protocolsn.registration.data.entity.PluginRegistrationEntity;
import coop.stlma.tech.protocolsn.registration.util.RepositoryTest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PluginRegistrationRepositoryTest extends RepositoryTest {

    @Inject
    PluginRegistrationRepository pluginRepository;

    @Test
    void testFindAll_happyPath() {
        pluginRepository.save(new PluginRegistrationEntity(null, "blog-plugin", "localhost:8082",
                null, null, null, null, null)).block();
        pluginRepository.save(new PluginRegistrationEntity(null, "blog-plugin-2", "localhost:8083",
                null, null, null, null, null)).block();
        List<PluginRegistrationEntity> products = pluginRepository.findAll().collectList().block();
        assertEquals(2, products.size());
    }
}
