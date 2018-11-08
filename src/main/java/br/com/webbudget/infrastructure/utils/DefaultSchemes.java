package br.com.webbudget.infrastructure.utils;

/**
 * The default database schemes
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 23/09/2018
 */
public interface DefaultSchemes {

    // the configuration
    String CONFIGURATION = "configuration";
    String CONFIGURATION_AUDIT = "configuration_audit";

    // the registration schema
    String REGISTRATION = "registration";
    String REGISTRATION_AUDIT = "registration_audit";

    // the journal schema
    String JOURNAL = "journal";
    String JOURNAL_AUDIT = "journal_audit";

    // the financial schema
    String FINANCIAL = "financial";
    String FINANCIAL_AUDIT = "financial_audit";
}
