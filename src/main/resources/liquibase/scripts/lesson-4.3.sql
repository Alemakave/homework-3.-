-- liquibase formatted sql

-- changeset alemakave:1
CREATE INDEX name_students_index ON student (name)

-- changeset alemakave:2
CREATE INDEX faculty_index ON faculty (name, color)