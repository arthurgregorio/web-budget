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
package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.CostCenter;
import br.com.webbudget.domain.misc.model.Page;
import br.com.webbudget.domain.misc.model.PageRequest;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 04/03/2013
 */
public interface ICostCenterRepository extends IGenericRepository<CostCenter, Long> {

    /**
     *
     * @param isBlocked
     * @return
     */
    public List<CostCenter> listByStatus(Boolean isBlocked);

    /**
     *
     * @param name
     * @param parent
     * @return
     */
    public CostCenter findByNameAndParent(String name, CostCenter parent);
    
    /**
     * 
     * @param isBlocked
     * @param pageRequest
     * @return 
     */
    public Page<CostCenter> listLazilyByStatus(Boolean isBlocked, PageRequest pageRequest);
}
