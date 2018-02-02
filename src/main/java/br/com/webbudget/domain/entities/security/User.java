package br.com.webbudget.domain.entities.security;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.infraestructure.shiro.UserDetails;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/12/2017
 */
@Entity
@ToString
@NoArgsConstructor
@Table(name = "users")
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
    
    @Transient
    @NotNull(message = "{user.password}")
    private String passwordConfirmation;

    /**
     *
     * @param name
     * @param email
     * @param username
     * @param blocked
     * @param group
     * @param profile
     */
    private User(String name, String email, String username, boolean blocked) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.blocked = blocked;
    }

    /**
     *
     * @param exampleData
     * @return
     */
    public static User asExample(String exampleData) {
        if (exampleData == null) {
            exampleData = "";
        }
        return new User(exampleData, exampleData, exampleData, false);
    }

    /**
     *
     * @return
     */
    public static SingularAttribute<User, ?>[] filterProperties() {
        return new SingularAttribute[]{User_.name, User_.username, User_.email};
    }

    /**
     * 
     * @return 
     */
    public Set<String> getPermissions() {
        return this.group != null ? this.group.getPermissions() : new HashSet<>();
    }

    /**
     * @return o nome do grupo deste usuario
     */
    public String getGroupName() {
        return this.group != null ? this.group.getName() : null;
    }
}
