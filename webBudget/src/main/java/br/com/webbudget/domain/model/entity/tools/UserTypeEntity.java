/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.model.entity.tools;

import br.com.webbudget.domain.model.entity.IPersistentEntity;
import br.com.webbudget.domain.model.security.User;
import javax.persistence.Column;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@Entity
@Table(name = "users")
@IdentityManaged(User.class)
public class UserTypeEntity extends AbstractIdentityTypeEntity implements IPersistentEntity<String> {

    @Getter
    @Setter
    @AttributeValue
    @Column(name = "username")
    private String username;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "name")
    private String name;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "email")
    private String email;
    
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "theme", nullable = false)
    private String theme;
    @Getter
    @Setter
    @AttributeValue
    @Column(name = "menu_layout")
    private String menuLayout;
  
    @Getter
    @Setter
    @OwnerReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_partition")
    private PartitionTypeEntity partition;

    /**
     * @return se esta entidade ja foi ou nao salva
     */
    @Override
    public boolean isSaved() {
        return this.getId() != null && !this.getId().isEmpty();
    }
}
