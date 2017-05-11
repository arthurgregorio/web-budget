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
package br.com.webbudget.application.producer;

import br.com.webbudget.application.producer.qualifier.DefaultDatabase;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.picketlink.annotations.PicketLink;

/**
 * Producer de entitymanagers para os recursos do projeto
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.0.0, 21/05/2015
 */
@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceUnit
    private EntityManagerFactory factory;

    /**
     *
     * @return
     */
    @Produces
    @PicketLink
    @DefaultDatabase
    EntityManager produce() {
        return this.factory.createEntityManager();
    }

    /**
     * Encerra um entityManager ja utilizado pelo sistema
     *
     * @param entityManager o entity manager a ser encerrado
     */
    void dispose(@Disposes @DefaultDatabase EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.clear();
            entityManager.close();
        }
    }
}
