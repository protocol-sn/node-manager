package coop.stlma.tech.protocolsn.nodemanager.health;

import coop.stlma.tech.protocolsn.nodemanager.health.service.PluginHealthService;
import coop.stlma.tech.protocolsn.nodemanager.registration.service.PluginRegistrationService;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * Do health checks for registered plugins
 *
 * @author John Meyerin
 */
@Singleton
@Slf4j
@Requires(property = "coop.stlma.tech.protocolsn.health-check.enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
public class HealthCron {

    private final PluginRegistrationService pluginRegistrationService;
    private final PluginHealthService pluginHealthService;

    public HealthCron(PluginRegistrationService pluginRegistrationService,
                      PluginHealthService pluginHealthService) {
        this.pluginRegistrationService = pluginRegistrationService;
        this.pluginHealthService = pluginHealthService;
    }

    /**
     * Periodically check the health of registered plugins
     */
    @Scheduled(cron = "${coop.stlma.tech.protocolsn.health.cron:*/10 * * * *}")
    void execute() {
        log.debug("Running health check");
        pluginHealthService.healthCheckAllPlugins()
            .flatMap(objects -> pluginRegistrationService.updateHealthStatus(objects.getT1(), objects.getT2()))
            .subscribe();
    }
}
