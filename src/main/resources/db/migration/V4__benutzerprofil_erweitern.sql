ALTER TABLE benutzer
    ADD COLUMN vorname varchar(255),
    ADD COLUMN nachname varchar(255);

ALTER TABLE nachricht
    ALTER COLUMN absender_id DROP NOT NULL,
    ALTER COLUMN empfaenger_id DROP NOT NULL;