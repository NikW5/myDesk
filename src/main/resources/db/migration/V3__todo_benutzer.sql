ALTER TABLE todo
ADD COLUMN benutzer_id integer;

ALTER TABLE todo
ADD CONSTRAINT todo_foreignkey_benutzer
    FOREIGN KEY (benutzer_id)
    REFERENCES benutzer (id);
