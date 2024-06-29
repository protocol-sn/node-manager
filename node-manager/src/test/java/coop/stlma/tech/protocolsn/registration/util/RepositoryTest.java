package coop.stlma.tech.protocolsn.registration.util;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Map;

public abstract class RepositoryTest implements TestPropertyProvider {

    @Inject
    ApplicationContext applicationContext;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16.3"
    );

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
        return Map.of("datasources.default.driver-class-name", "org.postgresql.Driver",
                "datasources.default.url", postgres.getJdbcUrl(),
                "datasources.default.username", postgres.getUsername(),
                "datasources.default.password", postgres.getPassword(),
                "flyway.datasources.default.enabled", "true");
    }

    @AfterEach
    public void tearDown() {
        applicationContext.getBeansOfType(ReactorCrudRepository.class)
                .forEach(ReactorCrudRepository::deleteAll);
    }
}
