/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.model.repository.logbook;

import br.com.webbudget.domain.model.entity.logbook.Entry;
import br.com.webbudget.domain.model.entity.logbook.Vehicle;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 05/06/2016
 */
public class EntryRepository extends GenericRepository<Entry, Long> implements IEntryRepository {

    /**
     *
     * @param code
     * @return
     */
    @Override
    public Entry findByMovementCode(String code) {

        final Criteria criteria = this.createCriteria();

        criteria.add(Restrictions.eq("financial", true));
        criteria.add(Restrictions.eq("movementCode", code));

        return (Entry) criteria.uniqueResult();
    }

    /**
     *
     * @param vehicle
     * @return
     */
    @Override
    public List<Entry> listByVehicle(Vehicle vehicle) {

        final Criteria criteria = this.createCriteria();

        criteria.createAlias("vehicle", "v");
        criteria.add(Restrictions.eq("v.id", vehicle.getId()));

        return criteria.list();
    }

    /**
     *
     * @param vehicle
     * @param filter
     * @return
     */
    @Override
    public List<Entry> listByVehicleAndFilter(Vehicle vehicle, String filter) {

        final Criteria criteria = this.createCriteria();

        criteria.createAlias("vehicle", "v");
        criteria.add(Restrictions.eq("v.id", vehicle.getId()));

        if (StringUtils.isNoneBlank(filter)) {
            criteria.createAlias("movementClass", "mc");
            criteria.add(Restrictions.or(
                    Restrictions.ilike("place", filter + "%"),
                    Restrictions.ilike("title", filter + "%"),
                    Restrictions.ilike("mc.name", filter + "%"),
                    Restrictions.ilike("description", "%" + filter + "%")));
        }

        return criteria.list();
    }
}
