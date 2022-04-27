CREATE TABLE SOURCES
(
    id           SERIAL PRIMARY KEY,
    url          VARCHAR(2048) NOT NULL,
    refresh_time INT
);

CREATE TABLE TAGS
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    created     timestamp without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);

CREATE TABLE CHARTS
(
    id          SERIAL PRIMARY KEY,
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
    user_name   VARCHAR(255),
    source_id   INT,
    created     timestamp without time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
    CONSTRAINT fk_source
        FOREIGN KEY(source_id)
            REFERENCES SOURCES(id)
);

CREATE INDEX IDX_CHARTS ON CHARTS(user_name, name, version);
