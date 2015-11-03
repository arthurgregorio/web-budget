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

import br.com.webbudget.domain.entity.movement.FixedMovement;
import br.com.webbudget.domain.entity.movement.Launch;
import br.com.webbudget.domain.entity.movement.Movement;
import br.com.webbudget.domain.misc.model.Page;
import br.com.webbudget.domain.misc.model.PageRequest;
import br.com.webbudget.domain.repository.IGenericRepository;
import java.util.List;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 20/09/2015
 */
public interface ILaunchRepository extends IGenericRepository<Launch, Long> {

    /**
     * 
     * @param movement
     * @return 
     */
    public Launch findByMovement(Movement movement);
    
    /**
     * 
     * @param fixedMovement
     * @return 
     */
    public Long countByFixedMovement(FixedMovement fixedMovement);
    
    /**
     * 
     * @param fixedMovement
     * @return 
     */
    public List<Launch> listByFixedMovement(FixedMovement fixedMovement);
    
    /**
     * 
     * @param fixedMovement
     * @param pageRequest
     * @return 
     */
    public Page<Launch> listByFixedMovement(FixedMovement fixedMovement, PageRequest pageRequest);
}
