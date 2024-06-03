package coop.stlma.tech.protocolsn.registration.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "registered_plugins")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PluginRegistrationEntity {

    @Id // <3>
    private UUID id;

    @Column(name = "plugin_name")
    private String pluginName;

    @Column(name = "plugin_location")
    private String pluginLocation;
}
