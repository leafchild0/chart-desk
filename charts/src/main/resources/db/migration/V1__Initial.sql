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
    user_id     VARCHAR(255),
    created     timestamp without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);

CREATE INDEX IDX_CHARTS ON CHARTS(user_id, name, version);
