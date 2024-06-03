package coop.stlma.tech.protocolsn.security;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.security.token.DefaultRolesFinder;
import io.micronaut.security.token.RolesFinder;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
@Replaces(DefaultRolesFinder.class)
public class KeycloakRoleFinder implements RolesFinder {

    @Override
    public List<String> resolveRoles(Map<String, Object> attributes) {
        //We don't necessarily know what types of Objects micronaut will render the attribute as, so this should be
        //implemented in a safer way. That is outside the scope of this spike and will be future John's problem.
        Map<String, Object> realmAccess = (Map<String, Object>) attributes.get("realm_access");
        if (realmAccess == null) {
            return Collections.emptyList();
        } else {
            return (List<String>) realmAccess.get("roles");
        }
    }
}
