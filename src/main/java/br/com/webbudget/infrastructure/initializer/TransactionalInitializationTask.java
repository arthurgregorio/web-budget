/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.initializer;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

/**
 * Transactional implementation of the {@link InitializationTask}, this class use a template method to perform the
 * initialization inside an {@link UserTransaction}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/03/2019
 */
public abstract class TransactionalInitializationTask implements InitializationTask {

    @Resource
    private UserTransaction transaction;

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            this.transaction.begin();
            this.runInsideTransaction();
            this.transaction.commit();
        } catch (Exception commitException) {
            try {
                this.transaction.rollback();
            } catch (Exception rollbackException) {
                throw new EJBException(rollbackException);
            }
            throw new EJBException(commitException);
        }
    }

    /**
     * Use this method to run the task in a {@link Transactional} environment
     */
    public abstract void runInsideTransaction();
}
