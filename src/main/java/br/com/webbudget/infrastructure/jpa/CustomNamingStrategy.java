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

package br.com.webbudget.infrastructure.jpa;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;

/**
 * Hibernate custom {@link ImplicitNamingStrategyJpaCompliantImpl} to make the FK and UK names more readable for humans
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 26/12/2018
 */
public class CustomNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    /**
     * {@inheritDoc}
     *
     * @param source
     * @return
     */
    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        return this.toIdentifier("fk_" + source.getTableName().getCanonicalName()
                + "_" + source.getReferencedTableName().getCanonicalName(), source.getBuildingContext());
    }

    /**
     * {@inheritDoc}
     *
     * @implNote this is not working due to this bug in Hibernate implementation
     *
     * @see <a href="https://hibernate.atlassian.net/browse/HHH-11586">HHH-11586</>
     *
     * @param source
     * @return
     */
    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        return this.toIdentifier("uk_" + source.getTableName().getCanonicalName(), source.getBuildingContext());
    }
}