/*
    Those are the schemas used by the application

    When postgres first start, they are created inside the database and in the next startups of the stack (compose)
    this script will be ignored, so, if you drop your database, you should run this script by yourself before start
    the application.

    Why this script didn't run automatically again or on every start?
    See here: https://hub.docker.com/_/postgres
    Search for "Initialization scripts"
*/

CREATE SCHEMA configuration;
CREATE SCHEMA configuration_audit;
CREATE SCHEMA financial;
CREATE SCHEMA financial_audit;
CREATE SCHEMA journal;
CREATE SCHEMA journal_audit;
CREATE SCHEMA registration;
CREATE SCHEMA registration_audit;