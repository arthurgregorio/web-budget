/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.tools;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * The {@link User} profile, this class hold the individual {@link User} preferences
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 16/07/2018
 */
@Entity
@Audited
@Table(name = "profiles")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AuditTable(value = "audit_profiles")
public class Profile extends PersistentEntity {

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "active_theme", nullable = false, length = 45)
    private ThemeType activeTheme;
    @Getter
    @Setter
    @Column(name = "dark_sidebar", nullable = false)
    private boolean userDarkSidebar;
    @Getter
    @Setter
    @Column(name = "show_wallet_balances", nullable = false)
    private boolean showWalletBalances;

    /**
     * Constructor
     */
    public Profile() {
        this.activeTheme = ThemeType.BLACK;
        this.showWalletBalances = true;
    }
}

