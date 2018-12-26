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

import org.hibernate.boot.model.naming.*;

/**
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 25/12/2018
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

        final Identifier identifier = super.determineForeignKeyName(source);

        System.out.println(source.getColumnNames());
        System.out.println("FK = " + identifier.getText());

        return identifier;
    }

    @Override
    public Identifier determineDiscriminatorColumnName(ImplicitDiscriminatorColumnNameSource source) {

        final Identifier identifier = super.determineDiscriminatorColumnName(source);

        System.out.println("?? = " + identifier.getText());

        return identifier;
    }

    /**
     * {@inheritDoc}
     *
     * @param source
     * @return
     */
    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {

        final Identifier identifier = super.determineUniqueKeyName(source);

        System.out.println("UK = " + identifier.getText());

        return identifier;
    }
}
