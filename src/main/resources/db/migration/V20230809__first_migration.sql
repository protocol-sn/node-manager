DROP TABLE IF EXISTS plugins;

CREATE TABLE IF NOT EXISTS plugins (
    id SERIAL PRIMARY KEY,
    pluginId varchar(256),
    pluginName varchar(256),
    baseUri varchar(1024)
);