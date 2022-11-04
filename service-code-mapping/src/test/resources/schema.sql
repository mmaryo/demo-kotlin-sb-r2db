CREATE SCHEMA IF NOT EXISTS "common";

----

CREATE TABLE IF NOT EXISTS "common"."CryptoExchanges"
(
    "Id"                SERIAL NOT NULL,
    "KaikoExchangeCode" character varying(30)
    );

INSERT INTO "common"."CryptoExchanges"
VALUES (DEFAULT, 'cryptoExchange');
INSERT INTO "common"."CryptoExchanges"
VALUES (DEFAULT, DEFAULT);

----

CREATE TABLE IF NOT EXISTS "common"."Currencies"
(
    "Id"      SERIAL                 NOT NULL,
    "IsoCode" character varying(100) NOT NULL
    );

INSERT INTO "common"."Currencies"
VALUES (DEFAULT, 'cryptoCode');

----

CREATE TABLE IF NOT EXISTS "common"."Entities"
(
    "Id"   SERIAL PRIMARY KEY,
    "Name" character varying(100) NOT NULL,
    "Code" character varying(20)  NOT NULL
    );

INSERT INTO "common"."Entities" VALUES
                                    (DEFAULT, 'Kaiko', 'Kaiko'),
                                    (DEFAULT, 'CoinMarketCap', 'CoinMarketCap');

----

CREATE TABLE IF NOT EXISTS "common"."ExchangeCurrencyMappings"
(
    "Id"               SERIAL PRIMARY KEY,
    "CryptoExchangeId" integer NOT NULL,
    "CurrencyId"       integer NOT NULL,
    "Mapping"          text
);

INSERT INTO IF NOT EXISTS "common"."ExchangeCurrencyMappings"
VALUES (DEFAULT, 66, 77, 'mapping');
