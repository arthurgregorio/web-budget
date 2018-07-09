package br.com.webbudget.infrastructure.cdi.qualifiers;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Injection qualifier to mark the field to receive the current principal username
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 13/12/2017
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface PrincipalUsername { }
