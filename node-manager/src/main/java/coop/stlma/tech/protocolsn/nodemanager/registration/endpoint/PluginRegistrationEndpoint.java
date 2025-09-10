package coop.stlma.tech.protocolsn.nodemanager.registration.endpoint;

import coop.stlma.tech.protocolsn.nodemanager.registration.service.PluginRegistrationService;
import coop.stlma.tech.protocolsn.nodemanager.PluginRegistration;
import coop.stlma.tech.protocolsn.nodemanager.RegistrationGrpc;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import lombok.extern.slf4j.Slf4j;

@GrpcService
@Slf4j
public class PluginRegistrationEndpoint extends RegistrationGrpc.RegistrationImplBase {

    private final PluginRegistrationService pluginRegistrationService;

    public PluginRegistrationEndpoint(PluginRegistrationService pluginRegistrationService) {
        this.pluginRegistrationService = pluginRegistrationService;
    }

    @Override
    public void registerPlugin(PluginRegistration request,
                               StreamObserver<PluginRegistration> responseObserver) {
        log.debug("registering plugin {}", request.getPluginName());
        responseObserver.onNext(pluginRegistrationService.registerPlugin(request).block());

        responseObserver.onCompleted();
    }
}
