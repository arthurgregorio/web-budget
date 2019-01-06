/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.registration;

import br.com.webbudget.domain.entities.registration.Vehicle;
import br.com.webbudget.domain.entities.registration.Vehicle_;
import br.com.webbudget.domain.repositories.LazyDefaultRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The {@link Vehicle} repository
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 2.3.0, 05/06/2016
 */
@Repository
public interface VehicleRepository extends LazyDefaultRepository<Vehicle> {

    /**
     * Use this method to find a vehicle by the license plate
     *
     * @param licensePlate the license plate of the vehicle to find
     * @return an {@link Optional} of the {@link Vehicle}
     */
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    /**
     * Method to find by the last registered odometer for a given {@link Vehicle}
     *
     * @param vehicleId the id of the {@link Vehicle} to find the odometer
     * @return the value of the last odometer
     */
    @Query("SELECT MAX(ve.odometer) FROM Vehicle ve WHERE ve.id = ?1")
    long findLastOdometer(long vehicleId);

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<Vehicle, Boolean> getEntityStateProperty() {
        return Vehicle_.active;
    }

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Collection<Criteria<Vehicle, Vehicle>> getRestrictions(String filter) {
        return List.of(
                this.criteria().eqIgnoreCase(Vehicle_.identification, this.likeAny(filter)),
                this.criteria().eqIgnoreCase(Vehicle_.brand, this.likeAny(filter)),
                this.criteria().eqIgnoreCase(Vehicle_.model, this.likeAny(filter)),
                this.criteria().eqIgnoreCase(Vehicle_.licensePlate, this.likeAny(filter)));
    }
}
