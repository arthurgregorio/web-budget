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
package br.com.webbudget.application.components.filter;

import br.com.webbudget.domain.entities.PersistentEntity;
import java.util.Set;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Interface to enable the filter abilities on some class managed by JPA througt
 * the deltaspike repositories
 *
 * @param <T> the type bound for this interface
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 19/02/2018
 */
public interface Filterable<T extends PersistentEntity> {

    /**
     *
     * @return
     */
    Set<SingularAttribute<T, ?>> getSingularAttributes();
}
