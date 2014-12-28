package br.com.webbudget.application.components.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation utlizada para realizar o agrupamento das authorities via reflection
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 29/06/2014
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorityGroup {

    /**
     * @return o grupo da authority
     */
    String value() default "";
}
