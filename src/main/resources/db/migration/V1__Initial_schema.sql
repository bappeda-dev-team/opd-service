CREATE TABLE opd(
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    kode_opd            VARCHAR(255) UNIQUE NOT NULL,
    nama_opd            VARCHAR(255) NOT NULL,
    kode_opd_parent     VARCHAR(255),
    version             INTEGER NOT NULL,
    created_date        timestamp NOT NULL,
    last_modified_date  timestamp NOT NULL
);