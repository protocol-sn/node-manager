ALTER TABLE registered_plugins ADD COLUMN health_endpoint VARCHAR(255);
ALTER TABLE registered_plugins ADD COLUMN current_health_status VARCHAR(63);
ALTER TABLE registered_plugins ADD COLUMN current_health_description VARCHAR(255);
ALTER TABLE registered_plugins ADD COLUMN last_health_check TIMESTAMP;
ALTER TABLE registered_plugins ADD COLUMN last_healthy_response VARCHAR(63);