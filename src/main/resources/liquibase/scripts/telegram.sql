-- liquibase formatted sql

-- changeset krozhdev:1
CREATE TABLE notificationtask (id SERIAL PRIMARY KEY,
                                chatId BIGINT,
                                message VARCHAR (255),
                                dateTime timestamp
);
