package br.com.webbudget.domain.entity.users;

import br.com.webbudget.domain.entity.PersistentEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TODO cadastro de contatos/clientes e vinculo com movimentos
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 21/03/2014
 */
@Entity
@Table(name = "contacts")
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true, of = "name")
public class Contact extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 90)
    private String name;
}
