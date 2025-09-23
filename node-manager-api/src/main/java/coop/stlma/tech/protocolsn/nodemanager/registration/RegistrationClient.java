package coop.stlma.tech.protocolsn.nodemanager.registration;

import coop.stlma.tech.protocolsn.commonlib.async.AsyncTranslator;
import coop.stlma.tech.protocolsn.nodemanager.PluginRegistration;
import coop.stlma.tech.protocolsn.nodemanager.RegistrationGrpc;
import io.grpc.ManagedChannel;
import io.micronaut.grpc.annotation.GrpcChannel;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import java.util.concurrent.Executor;

/**
 * Client for registering plugins
 *
 * @author John Meyerin
 */
@Singleton
public class RegistrationClient {

    private final RegistrationGrpc.RegistrationFutureStub stub;
    private final RegistrationGrpc.RegistrationBlockingStub blockingStub;
    private final Executor executor;
    private final ManagedChannel channel;

    public RegistrationClient(@Named(TaskExecutors.IO) Executor executor, @GrpcChannel("node-manager") ManagedChannel channel) {
        this.executor = executor;
        this.channel = channel;
        this.stub = RegistrationGrpc.newFutureStub(channel);
        this.blockingStub = RegistrationGrpc.newBlockingStub(channel);
    }

    /**
     * Close the channel
     */
    public void closeChannel() {
        channel.shutdown();
    }

    /**
     * Register a plugin asynchronously
     * @param pluginRegistration    Information on the plugin to register to supply to the node manager
     * @return                      The registered plugin as understood by the node manager
     */
    public Publisher<PluginRegistration> registerPlugin(PluginRegistration pluginRegistration) {
        return AsyncTranslator.toPublisher(stub.registerPlugin(pluginRegistration), executor);
    }

    /**
     * Register a plugin synchronously
     * @param pluginRegistration    Information on the plugin to register to supply to the node manager
     * @return                      The registered plugin as understood by the node manager
     */
    public PluginRegistration registerPluginBlocking(PluginRegistration pluginRegistration) {
        return blockingStub.registerPlugin(pluginRegistration);
    }
}
