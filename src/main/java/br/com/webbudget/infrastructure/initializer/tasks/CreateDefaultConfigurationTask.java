/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.initializer.tasks;

import br.com.webbudget.application.components.dto.Color;
import br.com.webbudget.domain.entities.configuration.Configuration;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.repositories.configuration.ConfigurationRepository;
import br.com.webbudget.domain.repositories.registration.CostCenterRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import br.com.webbudget.infrastructure.initializer.InitializationTask;
import br.com.webbudget.infrastructure.initializer.TransactionalInitializationTask;
import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * {@link InitializationTask} to create the default system {@link Configuration}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/03/2019
 */
@Dependent
@Exclude(ifProjectStage = {ProjectStage.Production.class, ProjectStage.SystemTest.class})
public class CreateDefaultConfigurationTask extends TransactionalInitializationTask {

    @Inject
    private CostCenterRepository costCenterRepository;
    @Inject
    private MovementClassRepository movementClassRepository;
    @Inject
    private ConfigurationRepository configurationRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInsideTransaction() {

        // create the default cost center
        this.costCenterRepository.findByName("Cartão de Crédito")
                .ifPresentOrElse(costCenter -> {/* do nothing*/}, () -> {

                    final CostCenter costCenter = new CostCenter();

                    costCenter.setColor(Color.randomize());
                    costCenter.setName("Cartão de Crédito");

                    this.costCenterRepository.save(costCenter);
                });

        // create the default movement class
        this.movementClassRepository.findByNameAndCostCenter_name("Faturas", "Cartão de Crédito")
                .ifPresentOrElse(costCenter -> {/* do nothing*/}, () -> {

                    final MovementClass movementClass = new MovementClass();

                    movementClass.setName("Faturas");
                    movementClass.setCostCenter(this.costCenterRepository.findByName("Cartão de Crédito")
                            .orElseThrow(() -> new IllegalStateException("Configuration error! Can't find cost center")));
                    movementClass.setMovementClassType(MovementClassType.EXPENSE);

                    this.movementClassRepository.save(movementClass);
                });

        // update the configuration
        this.configurationRepository.findCurrent().ifPresentOrElse(configuration -> {/* do nothig */}, () -> {

            final Configuration configuration = new Configuration();

            configuration.setCreditCardClass(this.movementClassRepository
                    .findByNameAndCostCenter_name("Faturas", "Cartão de Crédito")
                    .orElseThrow(() -> new IllegalStateException("Configuration error! Can't find movement class")));

            this.configurationRepository.save(configuration);
        });
    }
}
