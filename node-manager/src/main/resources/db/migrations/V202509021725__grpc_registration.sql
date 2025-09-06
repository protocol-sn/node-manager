ALTER TABLE registered_plugins ADD COLUMN plugin_grpc_port INT;
ALTER TABLE registered_plugins ADD COLUMN last_time_healthy TIMESTAMP;

ALTER TABLE registered_plugins DROP COLUMN health_endpoint;