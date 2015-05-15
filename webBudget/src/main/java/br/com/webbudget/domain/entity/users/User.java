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
package br.com.webbudget.domain.entity.users;

import br.com.webbudget.domain.entity.PersistentEntity;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/10/2013
 */
@Entity
@Table(name = "users")
@ToString(callSuper = true, of = {"email", "username"})
@EqualsAndHashCode(callSuper = true, of = {"email", "username"})
public class User extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{user-account.name}")
    @Column(name = "name", length = 90, nullable = false)
    private String name;
    @Getter
    @Setter
    @Email(message = "{user-account.email}")
    @NotEmpty(message = "{user-account.email}")
    @Column(name = "email", length = 90, nullable = false)
    private String email;
    @Setter
    @Getter
    @NotEmpty(message = "{user-account.username}")
    @Column(name = "username", length = 45, nullable = false)
    private String username;
    @Getter
    @Setter
    @Column(name = "password", length = 64, nullable = false)
    private String password;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;

    /**
     * as permissoes do usuario, eager pq se for lazy quando o spring solicitar
     * as authoritys vai bater no proxy do hibernate e estourar um lazyinitiexp
     */
    @Getter
    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Permission> permissions;

    @Getter
    @Setter
    @Transient
    private boolean selected;
    @Getter
    @Setter
    @Transient
    private String unsecurePassword;
    @Getter
    @Setter
    @Transient
    private String unsecurePasswordConfirmation;
}
