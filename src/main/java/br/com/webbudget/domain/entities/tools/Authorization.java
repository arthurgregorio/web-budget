package br.com.webbudget.domain.entities.tools;

import br.com.webbudget.domain.entities.PersistentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
 * @since 1.0.0, 26/12/2017
 */
@Entity
@ToString
@NoArgsConstructor
@Table(name = "authorizations")
@EqualsAndHashCode(callSuper = true)
public class Authorization extends PersistentEntity {
    
    @Getter
    @Setter
    @Column(name = "functionality", nullable = false, length = 90)
    private String functionality;
    @Getter
    @Setter
    @Column(name = "permission", nullable = false, length = 90)
    private String permission;

    /**
     * 
     * @param functionality
     * @param permission 
     */
    public Authorization(String functionality, String permission) {
        this.functionality = functionality;
        this.permission = permission;
    }

    /**
     * @return a definicao completa da permissao
     */
    public String getFullPermission() {
        return this.functionality + ":" + this.permission;
    }
    
    /**
     * 
     * @param functionality
     * @return 
     */
    public boolean isFunctionality(String functionality) {
        return functionality != null && this.functionality.equals(functionality);
    }

    /**
     * 
     * @param permission
     * @return 
     */
    public boolean isPermission(String permission) {
        return permission != null && (this.permission.equals(permission) 
                || this.getFullPermission().equals(permission));
    }
}
