CREATE TABLE IF NOT EXISTS items
(
    id         VARCHAR(36) NOT NULL PRIMARY KEY,
    name       VARCHAR(25) NOT NULL,
    quantity   INT         NOT NULL,
    price      DOUBLE PRECISION,
    created_at DATETIME(3),
    updated_at DATETIME(3),
    version    BIGINT      not null default 0
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;