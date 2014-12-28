package br.com.webbudget.domain.entity.users;

import br.com.webbudget.domain.entity.PersistentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 24/06/2014
 */
@Entity
@Table(name = "permissions")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Permission extends PersistentEntity implements GrantedAuthority {

    @Setter
    @Column(name = "authority", nullable = false)
    private String authority;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    
    /**
     * 
     * @return 
     */
    @Override
    public String getAuthority() {
        return this.authority;
    }
}
