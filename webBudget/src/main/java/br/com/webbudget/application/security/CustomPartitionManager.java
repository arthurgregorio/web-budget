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
package br.com.webbudget.application.security;

import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.internal.DefaultPartitionManager;

/**
 * Uma implementacao customizada do partition manager para que o sistema possa
 * produzir uma versao customizada do gerenciador de relacionamentos
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.2, 23/12/2015
 */
public class CustomPartitionManager extends DefaultPartitionManager {
    
    /**
     * 
     * @param configuration 
     */
    public CustomPartitionManager(IdentityConfiguration configuration) {
        super(configuration);
    }

    /**
     * 
     * @return 
     */
    @Override
    public RelationshipManager createRelationshipManager() {
        return new CustomRelationshipManager(this);
    }
}
