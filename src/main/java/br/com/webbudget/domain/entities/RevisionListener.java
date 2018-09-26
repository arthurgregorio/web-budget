/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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

import org.apache.shiro.SecurityUtils;

/**
 * The listener to add more info to the revision of the audited entities
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/06/2018
 */
public class RevisionListener implements org.hibernate.envers.RevisionListener {

    /**
     * {@inheritDoc }
     *
     * @param revisionEntity
     */
    @Override
    public void newRevision(Object revisionEntity) {
        final Revision revision = (Revision) revisionEntity;
        revision.setCreatedBy(this.getLoggedUser());
    }

    /**
     * Get the username of the logged user
     *
     * @return the username of the logged user
     */
    private String getLoggedUser() {
        try {
            return String.valueOf(SecurityUtils.getSubject().getPrincipal());
        } catch (Exception ex) {
            return "unknown";
        }
    }
}