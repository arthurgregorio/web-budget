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
package br.com.webbudget.application.controller.configuration;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.entities.configuration.Configuration;
import br.com.webbudget.domain.entities.registration.MovementClass;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.configuration.ConfigurationRepository;
import br.com.webbudget.domain.repositories.registration.MovementClassRepository;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Controller used to manage the {@link Configuration} change at the user control sidebar
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 16/03/2019
 */
@Named
@ViewScoped
public class ConfigurationBean extends AbstractBean {

    @Getter
    private Configuration configuration;

    @Getter
    private List<MovementClass> movementClasses;

    @Inject
    private MovementClassRepository movementClassRepository;
    @Inject
    private ConfigurationRepository configurationRepository;

    /**
     * Initialize this bean
     */
    public void initialize() {
        this.movementClasses = this.movementClassRepository.findAllActive();
        this.configuration = this.configurationRepository.findCurrent()
                .orElseThrow(() -> new BusinessLogicException("error.configuration.not-found"));
    }

    /**
     * Save {@link Configuration}
     */
    @Transactional
    public void doSave() {
        this.configurationRepository.save(this.configuration);
        this.addInfo(true, "info.configuration.saved");
    }

    /**
     * Update {@link Configuration}
     */
    @Transactional
    public void update() {
        this.configurationRepository.saveAndFlushAndRefresh(this.configuration);
        this.addInfo(true, "info.configuration.updated");
    }
}
