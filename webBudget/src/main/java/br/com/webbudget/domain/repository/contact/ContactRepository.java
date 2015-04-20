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
package br.com.webbudget.domain.repository.contact;

import br.com.webbudget.domain.entity.contact.Contact;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 12/04/2015
 */
@Repository
public class ContactRepository extends GenericRepository<Contact, Long> implements IContactRepository {

    /**
     *
     * @param blocked
     * @return
     */
    @Override
    public List<Contact> listByStatus(Boolean blocked) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }

        return criteria.list();
    }
    
    /**
     * 
     * @param filter
     * @param blocked
     * @return 
     */
    @Override
    public List<Contact> listByFilter(String filter, Boolean blocked) {
        
        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (filter != null) {
            criteria.add(Restrictions.or(
                    Restrictions.ilike("name", "%" + filter + "%"),
                    Restrictions.ilike("email", "%" + filter + "%"),
                    Restrictions.eq("document", filter),
                    Restrictions.ilike("city", "%" + filter + "%")
            ));
        }

        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }
        
        return criteria.list();
    }
}
