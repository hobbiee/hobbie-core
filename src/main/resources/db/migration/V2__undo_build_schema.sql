-- the file V1__build_schema.sql is the first migration script that creates the schema
-- but for some reason it is running as if the property spring.jpa.hibernate.ddl-auto=create-drop
-- was set in the application.properties file (which it is not in dev and prod profile)

-- the file V2__undo_build_schema.sql purpose is to undo the changes made by V1__build_schema.sql

-- drop all sequences

DROP SEQUENCE IF EXISTS hibernate_sequence;

DROP SEQUENCE IF EXISTS event_seq;

DROP SEQUENCE IF EXISTS join_request_seq;

DROP SEQUENCE IF EXISTS tag_seq;

-- drop all constraints
ALTER TABLE IF EXISTS event
    DROP CONSTRAINT IF EXISTS FK_EVENT_ON_ADMIN;

ALTER TABLE IF EXISTS join_request
    DROP CONSTRAINT IF EXISTS FK_JOINREQUEST_ON_EVENT;

ALTER TABLE IF EXISTS join_request
    DROP CONSTRAINT IF EXISTS FK_JOINREQUEST_ON_PLAYER;

ALTER TABLE IF EXISTS event_categories
    DROP CONSTRAINT IF EXISTS fk_evecat_on_event;

ALTER TABLE IF EXISTS event_categories
    DROP CONSTRAINT IF EXISTS fk_evecat_on_tag;

ALTER TABLE IF EXISTS event_participants
    DROP CONSTRAINT IF EXISTS fk_evepar_on_event;

ALTER TABLE IF EXISTS event_participants
    DROP CONSTRAINT IF EXISTS fk_evepar_on_player;

ALTER TABLE IF EXISTS player_interests
    DROP CONSTRAINT IF EXISTS fk_plaint_on_player;

ALTER TABLE IF EXISTS player_interests
    DROP CONSTRAINT IF EXISTS fk_plaint_on_tag;

-- drop all tables

DROP TABLE IF EXISTS join_request;

DROP TABLE IF EXISTS player_interests;

DROP TABLE IF EXISTS event_participants;

DROP TABLE IF EXISTS event_categories;

DROP TABLE IF EXISTS event;

DROP TABLE IF EXISTS player;

DROP TABLE IF EXISTS tag;

