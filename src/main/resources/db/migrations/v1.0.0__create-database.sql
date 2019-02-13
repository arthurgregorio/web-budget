/*
  Use this to create the database schemas, prefer the ones without the authorization part and always use a specific
  user to the application without the super powers
*/

/*
CREATE SCHEMA financial AUTHORIZATION sa_webbudget;
CREATE SCHEMA financial_audit AUTHORIZATION sa_webbudget;

CREATE SCHEMA journal AUTHORIZATION sa_webbudget;
CREATE SCHEMA journal_audit AUTHORIZATION sa_webbudget;

CREATE SCHEMA registration AUTHORIZATION sa_webbudget;
CREATE SCHEMA registration_audit AUTHORIZATION sa_webbudget;

CREATE SCHEMA configuration AUTHORIZATION sa_webbudget;
CREATE SCHEMA configuration_audit AUTHORIZATION sa_webbudget;
*/

CREATE SCHEMA financial;
CREATE SCHEMA financial_audit;

CREATE SCHEMA journal;
CREATE SCHEMA journal_audit;

CREATE SCHEMA registration;
CREATE SCHEMA registration_audit;

CREATE SCHEMA configuration;
CREATE SCHEMA configuration_audit;