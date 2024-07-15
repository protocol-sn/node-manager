DROP TABLE IF EXISTS registered_plugins;

CREATE TABLE registered_plugins (
    id UUID PRIMARY KEY,
    plugin_name VARCHAR(255) NOT NULL,
    plugin_location VARCHAR(255) NOT NULL
);