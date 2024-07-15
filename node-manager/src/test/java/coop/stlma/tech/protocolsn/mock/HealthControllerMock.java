package coop.stlma.tech.protocolsn.mock;

import coop.stlma.tech.protocolsn.health.api.HealthOperations;
import coop.stlma.tech.protocolsn.health.model.HealthResponse;
import coop.stlma.tech.protocolsn.health.model.HealthStatus;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Controller
@Requires(property = "activeTest", value = "HealthControllerTest")
public class HealthControllerMock implements HealthOperations {

    @Value("${healthResponse:healthy}")
    String healthResponse = "healthy";

    public HealthControllerMock() {
        System.out.println("HealthControllerMock: " + healthResponse);
    }

    @Override
    @Get(HealthOperations.HEALTH_ENDPOINT)
    public Publisher<HttpResponse<HealthResponse>> healthCheck() {
        System.out.println("Returning mock health response for " + healthResponse);
        if ("healthy".equals(healthResponse)) {
            return Mono.just(HttpResponse.ok(new HealthResponse(HealthStatus.HEALTHY, "I am healthy")));
        }
        return Mono.just(HttpResponse.ok(new HealthResponse(HealthStatus.UP_WITH_PROBLEMS, "cough cough")));
    }
}
