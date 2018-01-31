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
package br.com.webbudget.domain.entities;

import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

/**
 * Listener de edicao e persistencia dos dados, com ele preenchemos os valores
 * default de nossas entidades
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.2.0, 02/03/2016
  */
public class PersistentEntityListener {

    /**
     * Listerner de pre-persistencia do dados
     *
     * @param entity a entidade a ser afetada pelo evento
     */
    @PrePersist
    public void prePersist(PersistentEntity entity) {
        entity.setInclusion(LocalDateTime.now());
        
        if (StringUtils.isBlank(entity.getIncludedBy())) {
            entity.setIncludedBy(this.getLoggedUser());
        }
    }

    /**
     * Listerner de pre-atualizacao do dados
     *
     * @param entity a entidade a ser afetada pelo evento
     */
    @PreUpdate
    public void preUpdate(PersistentEntity entity) {
        entity.setLastEdition(LocalDateTime.now());
        
        if (StringUtils.isBlank(entity.getEditedBy())) {
            entity.setEditedBy(this.getLoggedUser());
        }
    }

    /**
     * @return o login do usuario logado
     */
    private String getLoggedUser() {
        return String.valueOf(SecurityUtils.getSubject().getPrincipal());
    }
}

