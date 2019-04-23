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
package br.com.webbudget.application.controller.registration;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The {@link Card} statistics controller
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.2.1, 06/05/2016
 */
@Named
@ViewScoped
public class CardStatisticsBean extends AbstractBean {

    @Getter
    private Card card;

    @Inject
    private CardRepository cardRepository;

    /**
     * Initialize this bean
     *
     * @param id of the {@link Card} we are about to see the statistics
     */
    public void initialize(Long id) {
        this.card = this.cardRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("error.card-statistics.not-found"));
    }
}
