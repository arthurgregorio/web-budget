package br.com.webbudget.domain.model.entity.converter;

import br.com.webbudget.domain.model.security.User;
import javax.persistence.AttributeConverter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 11/04/2016
 */
public class UserConverter implements AttributeConverter<User, String> {

    /**
     *
     * @param attribute
     * @return
     */
    @Override
    public String convertToDatabaseColumn(User attribute) {
        return attribute.getId();
    }

    /**
     *
     * @param dbData
     * @return
     */
    @Override
    public User convertToEntityAttribute(String dbData) {
        final User user = new User();
        user.setId(dbData);
        return user;
    }
}
