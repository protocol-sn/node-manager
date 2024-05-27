# Node Manager

## Starting up keycloak
Navigate to the keycloak directory and enter `docker-compose up`. This will start up keycloak with a default realm "Social-Network-Ecosystem".
This realm will include one user, "demo-user". Their password is "password".

## Starting up node manager
`./gradlew run`

## Logging in
With both keycloak and node manager running, go to "http://localhost:8080/oauth/login/keycloak" in your browser. This will redirect you to the keycloak login page. Use the credentials outlined above. You will be redirected back to this service.
