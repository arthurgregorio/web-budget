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

package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.permission.Authority;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import lombok.Getter;

/**
 * Bean utlizado pelo sistema para requisitar as authorities disponiveis no 
 * sistemas
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 29/06/2014
 */
@ManagedBean
@RequestScoped
public class PermissionsBean {

    @Getter
    private Authority authority;
    
    /**
     * Inicializa a authority
     */
    @PostConstruct
    protected void initialize() {
        this.authority = new Authority();
    }
}
