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

import br.com.webbudget.application.components.dto.Color;
import br.com.webbudget.application.components.dto.CreditCardInvoiceResume;
import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.application.components.ui.chart.LineChartDataset;
import br.com.webbudget.application.components.ui.chart.LineChartModel;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.view.PeriodResult;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import br.com.webbudget.infrastructure.utils.MessageSource;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    private boolean loaded;

    @Getter
    private Card card;
    @Getter
    private CreditCardInvoiceResume resume;

    @Getter
    private List<CreditCardInvoice> invoices;

    @Inject
    private CardRepository cardRepository;
    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * Initialize this bean
     *
     * @param id of the {@link Card} we are about to see the statistics
     */
    public void initialize(Long id) {

        // find the card
        this.card = this.cardRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("error.card-statistics.not-found"));

        // list invoices
        this.invoices = this.creditCardInvoiceRepository.findByCard(this.card);
    }

    /**
     * Load charts on the UI
     */
    public void loadChart() {

        this.resume = new CreditCardInvoiceResume();
        this.resume.load(invoices);

        this.loadValuesChart(invoices);



        this.loaded = true;
    }

    /**
     * Draw the chart about last six {@link CreditCardInvoice} values
     *
     * @param invoices the list of {@link CreditCardInvoice}
     */
    private void loadValuesChart(List<CreditCardInvoice> invoices) {

        final Color blue = new Color(30, 144, 255);

        final LineChartDataset<BigDecimal> valuesDataset = new LineChartDataset<>();

        valuesDataset.setLabel(MessageSource.get("card-statistics.chart.value"));
        valuesDataset.setBorderColor(blue.toString());
        valuesDataset.setBackgroundColor(blue.transparent().toString());
        valuesDataset.addAllData(invoices.stream()
                .map(CreditCardInvoice::getTotalValue)
                .collect(Collectors.toList()));

        final LineChartModel<BigDecimal> model = new LineChartModel<>();

        model.addDataset(valuesDataset);
        model.addAllLabels(invoices.stream()
                .map(invoice -> invoice.getFinancialPeriod().getIdentification())
                .collect(Collectors.toList()));

        this.executeScript("drawLineChart(" + model.toJson() + ", 'invoiceValuesChart')");
    }
}
