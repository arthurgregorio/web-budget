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

import br.com.webbudget.domain.model.security.Partition;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.picketlink.annotations.PicketLink;

/**
 * Um producer para os recursos necessarios ao Picketlink:
 * 
 * {@link EntityManager} para que o PL possa gerenciar nosso modelo de seguranca
 * baseado em JPA
 * {@link Partition} para que nossa producao do gerenciador de identidades ja 
 * saia configurada de fabrica
 * 
 * Recursos do PL sao identificados pela anotacao {@link PicketLink}
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
@ApplicationScoped
public class EntityManagerProducer {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * @return um entityManager configurado para o nosso webBudgetPU
     */
    @Produces
    @PicketLink
    EntityManager produceEntityManager() {
        return this.entityManager;
    }
}
