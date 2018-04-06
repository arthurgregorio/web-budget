package br.com.webbudget.domain.entities.security;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.eti.arthurgregorio.shiroee.config.jdbc.UserDetails;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/12/2017
 */
@Entity
@Table(name = "users")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends PersistentEntity implements UserDetails {

    @Getter
    @Setter
    @NotNull(message = "{user.name}")
    @Column(name = "name", length = 90, nullable = false)
    private String name;
    @Getter
    @Setter
    @NotNull(message = "{user.email}")
    @Column(name = "email", length = 90, nullable = false)
    private String email;
    @Getter
    @Setter
    @NotNull(message = "{user.username}")
    @Column(name = "username", length = 20, nullable = false)
    private String username;
    @Getter
    @Setter
    @NotNull(message = "{user.password}")
    @Column(name = "password", length = 60, nullable = false)
    private String password;
    @Getter
    @Setter
    @NotNull(message = "{user.blocked}")
    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{user.group}")
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;
    
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{user.store-type}")
    @Column(name = "store_type", nullable = false)
    private StoreType storeType;
    
    @Getter
    @Setter
    @Transient
    @NotNull(message = "{user.password}")
    private String passwordConfirmation;

    /**
     * 
     */
    public User() {
        this.storeType = StoreType.LOCAL;
    }
    
    /**
     *
     * @return 
     */
    @Override
    public boolean isLdapBindAccount() {
        return this.storeType == StoreType.LDAP;
    }

    /**
     * 
     * @return 
     */
    @Override
    public Set<String> getPermissions() {
        return this.group != null ? this.group.getPermissions() : new HashSet<>();
    }

    /**
     * @return o nome do grupo deste usuario
     */
    public String getGroupName() {
        return this.group != null ? this.group.getName() : null;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isPasswordValid() {
        return isNotBlank(this.password) 
                && isNotBlank(this.passwordConfirmation) 
                && this.password.equals(this.passwordConfirmation);
    }

    /**
     * 
     * @return 
     */
    public boolean hasChangedPasswords() {
        return isNotBlank(this.password) 
                && isNotBlank(this.passwordConfirmation);
    }
    
    /**
     * 
     * @return 
     */
    public boolean isAdministrator() {
        return this.username.equals("admin");
    }
}
