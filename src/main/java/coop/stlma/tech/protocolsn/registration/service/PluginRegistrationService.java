package coop.stlma.tech.protocolsn.registration.service;

import coop.stlma.tech.protocolsn.registration.domain.PluginRegistration;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface PluginRegistrationService {

    Mono<PluginRegistration> registerPlugin(PluginRegistration pluginRegistration);
}
