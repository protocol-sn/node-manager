package coop.stlma.tech.protocolsn.registration.domain;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable
public class PluginRegistration {
    private UUID id;
    private String pluginName;
    private String pluginLocation;
}
