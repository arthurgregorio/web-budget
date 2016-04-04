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
package br.com.webbudget.domain.model.repository.user;

import br.com.webbudget.domain.model.entity.message.PrivateMessage;
import br.com.webbudget.domain.model.security.User;
import br.com.webbudget.domain.model.entity.message.UserPrivateMessage;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 18/10/2013
 */
public class UserPrivateMessageRepository extends GenericRepository<UserPrivateMessage, Long> implements IUserPrivateMessageRepository {

    /**
     *
     * @param user
     * @param showUnread
     * @return
     */
    @Override
    public List<UserPrivateMessage> listByUser(User user, Boolean showUnread) {

        final Criteria criteria = this.createCriteria();

        criteria.createAlias("recipient", "u");
        criteria.add(Restrictions.eq("u.id", user.getId()));

        if (showUnread != null) {
            criteria.add(Restrictions.eq("wasRead", false));
        }

        // nao mostra mensagens deletadas nunca
        criteria.add(Restrictions.eq("deleted", false));

        criteria.addOrder(Order.desc("inclusion"));

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Override
    public List<UserPrivateMessage> listReceipts(PrivateMessage privateMessage) {

        final Criteria criteria = this.createCriteria();

        criteria.createAlias("privateMessage", "pm");
        criteria.add(Restrictions.eq("pm.id", privateMessage.getId()));

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }
}
