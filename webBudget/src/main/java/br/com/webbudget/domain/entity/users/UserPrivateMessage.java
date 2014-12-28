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

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 12/05/2014
 */
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_private_messages")
public class UserPrivateMessage extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "was_read")
    private boolean wasRead;
    @Getter
    @Setter
    @Column(name = "deleted")
    private boolean deleted;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_private_message")
    private PrivateMessage privateMessage;
}
