./gradlew clean bootJar
docker build -t protocol-sn-node-manager:latest .
docker compose up