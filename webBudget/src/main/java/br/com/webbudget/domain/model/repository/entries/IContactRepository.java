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
package br.com.webbudget.domain.model.repository.entries;

import br.com.webbudget.domain.model.entity.entries.Contact;
import br.com.webbudget.application.component.table.Page;
import br.com.webbudget.application.component.table.PageRequest;
import br.com.webbudget.domain.model.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 12/04/2015
 */
public interface IContactRepository extends IGenericRepository<Contact, Long> {

    /**
     *
     * @param blocked
     * @return
     */
    public List<Contact> listByStatus(Boolean blocked);
    
    /**
     * 
     * @param filter
     * @return 
     */
    public List<Contact> listByFilter(String filter, Boolean blocked);
    
    /**
     * 
     * @param filter
     * @param blocked
     * @param pageRequest
     * @return 
     */
    public Page<Contact> listLazilyByFilter(String filter, Boolean blocked, PageRequest pageRequest);
}
