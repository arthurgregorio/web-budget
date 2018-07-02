package br.com.webbudget.domain.entities.tools;

import br.com.webbudget.domain.entities.PersistentEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 26/12/2017
 */
@Entity
@Audited
@ToString
@NoArgsConstructor
@Table(name = "grants")
@AuditTable(value = "audit_grants")
@EqualsAndHashCode(callSuper = true)
public class Grant extends PersistentEntity {

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_authorization", nullable = false)
    private Authorization authorization;

    /**
     * 
     * @param group
     * @param authorization 
     */
    public Grant(Group group, Authorization authorization) {
        this.group = group;
        this.authorization = authorization;
    }
}
