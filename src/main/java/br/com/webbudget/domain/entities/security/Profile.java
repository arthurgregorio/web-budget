package br.com.webbudget.domain.entities.security;

import br.com.webbudget.domain.entities.PersistentEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 26/12/2017
 */
@Entity
@ToString
@Table(name = "profiles")
@EqualsAndHashCode(callSuper = true)
public class Profile extends PersistentEntity {

    /**
     * @return o profile default para qualquer user
     */
    public static Profile createDefault() {
        
        final Profile profile = new Profile();
        
        return profile;
    }
}
