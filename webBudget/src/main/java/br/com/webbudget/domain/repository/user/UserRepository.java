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
package br.com.webbudget.domain.repository.user;

import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 18/10/2013
 */
public class UserRepository extends GenericRepository<User, Long> implements IUserRepository {

    /**
     *
     * @param login
     * @return
     */
    @Override
    public User findByUsername(String login) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.add(Restrictions.eq("username", login));

        return (User) criteria.uniqueResult();
    }

    /**
     *
     * @param blocked
     * @return
     */
    @Override
    public List<User> listByStatus(Boolean blocked) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }

        return criteria.list();
    }

    /**
     *
     * @param blocked
     * @param authenticated
     * @return
     */
    @Override
    public List<User> listByStatusAndRemoveAuthenticated(Boolean blocked, User authenticated) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        if (blocked != null) {
            criteria.add(Restrictions.eq("blocked", blocked));
        }

        criteria.add(Restrictions.ne("id", authenticated.getId()));

        return criteria.list();
    }
}
