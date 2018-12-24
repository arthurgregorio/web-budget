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
package br.com.webbudget.domain.repositories.financial;

import br.com.webbudget.domain.entities.financial.FixedMovement;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.1.0, 20/09/2015
 */
@Repository
public interface IFixedMovementRepository extends EntityRepository<FixedMovement, Long> {

//    /**
//     * 
//     * @return 
//     */
//    public List<FixedMovement> listAutoLaunch();
//    
//    /**
//     * 
//     * @param text
//     * @param pageRequest
//     * @return 
//     */
//    public Page<FixedMovement> listByFilter(String text, PageRequest pageRequest);
}
