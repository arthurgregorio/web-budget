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
package br.com.webbudget.domain.repository.system;

import br.com.webbudget.domain.entity.system.Configuration;
import br.com.webbudget.domain.repository.GenericRepository;
import org.hibernate.Criteria;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/10/2013
 */
public class ConfigurationRepository extends GenericRepository<Configuration, Long> implements IConfigurationRepository {

    /**
     *
     * @return
     */
    @Override
    public Configuration findDefault() {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        return (Configuration) criteria.uniqueResult();
    }
}
