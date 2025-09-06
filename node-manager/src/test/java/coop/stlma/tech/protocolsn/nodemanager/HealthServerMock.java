package coop.stlma.tech.protocolsn.nodemanager;

import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import coop.stlma.tech.protocolsn.pluginlib.Empty;
import coop.stlma.tech.protocolsn.pluginlib.HealthGrpc;
import coop.stlma.tech.protocolsn.pluginlib.HealthResponse;
import io.grpc.stub.StreamObserver;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

@Singleton
@Requires(property = "activeTest", value = "HealthServerMock")
public class HealthServerMock extends HealthGrpc.HealthImplBase {

    public static HealthStatus healthStatus = HealthStatus.HEALTHY;
    public static String description = "I am healthy";

    @Override
    public void healthCheck(Empty empty, StreamObserver<HealthResponse> responseObserver) {
        responseObserver.onNext(HealthResponse.newBuilder()
                .setHealthStatus(healthStatus.name())
                .setDescription(description)
                .build());

        responseObserver.onCompleted();
    }
}
