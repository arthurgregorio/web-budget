package br.com.webbudget.application.components.validators;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 25/01/2015
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
@Constraint(validatedBy = FieldMatchValidator.class)
public @interface MatchFields {

    String first();

    String second();
    
    Class<?>[] groups() default {};
    
    String message() default "The fields not match";

    Class<? extends Payload>[] payload() default {};

    /**
     * 
     */
    @Documented
    @Retention(RUNTIME)
    @Target({TYPE, ANNOTATION_TYPE})
    @interface List {
        MatchFields[] value();
    }
}
