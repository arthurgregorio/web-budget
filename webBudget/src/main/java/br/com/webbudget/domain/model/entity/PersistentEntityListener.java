/*
 * Copyright (C) 2016 arthur
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

package br.com.webbudget.domain.model.entity;

import br.com.webbudget.domain.model.security.User;
import java.util.Date;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.omnifaces.util.BeansLocal;
import org.picketlink.Identity;

/**
 * Listener de edicao e persistencia dos dados, com ele preenchemos os valores
 * default de nossas entidades
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 02/03/2016
  */
public class PersistentEntityListener {

    @Inject
    private BeanManager beanManager;

    /**
     * Listerner de pre-persistencia do dados
     * 
     * @param entity a entidade a ser afetada pelo evento
     */
    @PrePersist
    public void prePersist(PersistentEntity entity) {
        entity.setInclusion(new Date());
        entity.setIncludedBy(this.getAuthenticated().getUsername());
    }
    
    /**
     * Listerner de pre-atualizacao do dados
     * 
     * @param entity a entidade a ser afetada pelo evento
     */
    @PreUpdate
    public void preUpdate(PersistentEntity entity) {
        entity.setLastEdition(new Date());
        entity.setEditedBy(this.getAuthenticated().getUsername());
    }
    
    /**
     * @return o usuario autenticado
     */
    private User getAuthenticated() {

        final Identity identity = BeansLocal.getInstance(
                this.beanManager, Identity.class);
        
        return (User) identity.getAccount();
    }
}

