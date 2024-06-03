package coop.stlma.tech.protocolsn;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(info = @Info(
        title = "Protocol SN node manager",
        version = "0.0.2"
))
@SecuritySchemes(
        @SecurityScheme(
                name = "pluginClient",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(
                        clientCredentials = @OAuthFlow(
                                tokenUrl = "${coop.stlma.tech.protocolsn.security.tokenUrl:http://localhost:9080/realms/spike-realm/protocol/openid-connect/token}"
                        )
                )
        ))
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}