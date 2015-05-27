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
package br.com.webbudget.domain.security;

import org.picketlink.idm.model.AbstractPartition;
import org.picketlink.idm.model.annotation.IdentityPartition;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@IdentityPartition(supportedTypes = {User.class, Role.class, Group.class})
public class Partition extends AbstractPartition {

    public static final String DEFAULT = "webBudget";

    /**
     * 
     */
    private Partition() { 
        super(null); 
    }

    /**
     * 
     * @param name 
     */
    public Partition(String name) {
        super(name);
    }
}
