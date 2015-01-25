package br.com.webbudget.application.components.validators;

import java.lang.reflect.InvocationTargetException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 25/01/2015
 */
public class FieldMatchValidator implements ConstraintValidator<MatchFields, Object> {

    private String firstFieldName;
    private String secondFieldName;

    /**
     * 
     * @param constraintAnnotation 
     */
    @Override
    public void initialize(MatchFields constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    /**
     * 
     * @param value
     * @param context
     * @return 
     */
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        try {
            
            final BeanUtilsBean beanUtilsBean = BeanUtilsBean2.getInstance();
            
            final Object firstObj = beanUtilsBean.getProperty(value, this.firstFieldName);
            final Object secondObj = beanUtilsBean.getProperty(value, this.secondFieldName);

            return firstObj == null && secondObj == null 
                    || firstObj != null && firstObj.equals(secondObj);
        } catch (IllegalAccessException | InvocationTargetException 
                | NoSuchMethodException ex) { }
        
        return true;
    }
}
