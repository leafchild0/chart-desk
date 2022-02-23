CREATE TABLE CHARTS
(
    id          SERIAL,
    name        VARCHAR(255),
    version     VARCHAR(255),
    digest      VARCHAR(255),
    description TEXT,
    app_version VARCHAR(255),
    icon        VARCHAR(2048),
    engine      VARCHAR(2048),
    home        VARCHAR(2048),
    keywords    TEXT,
    sources     TEXT,
    maintainers TEXT,
    urls        TEXT,
    prov_urls   TEXT,
    created     timestamp without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);
