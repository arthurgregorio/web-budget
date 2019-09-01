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
import br.com.webbudget.application.components.ui.chart.PieChartDataset;
import br.com.webbudget.application.components.ui.chart.PieChartModel;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.view.CardConsume;
import br.com.webbudget.domain.entities.view.CardConsumeDetailed;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import br.com.webbudget.domain.repositories.view.CardConsumeDetailedRepository;
import br.com.webbudget.domain.repositories.view.CardConsumeRepository;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.webbudget.application.components.ui.chart.ChartUtils.percentageOf;

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
    @Getter
    private List<CardConsumeDetailed> cardConsumesDetailed;

    @Inject
    private CardRepository cardRepository;
    @Inject
    private CardConsumeRepository cardConsumeRepository;
    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;
    @Inject
    private CardConsumeDetailedRepository cardConsumeDetailedRepository;

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
    public void loadCharts() {

        this.resume = new CreditCardInvoiceResume();
        this.resume.load(this.invoices);

        this.loadValuesChart();
        this.loadConsumeChart();

        this.cardConsumesDetailed = this.cardConsumeDetailedRepository.findByCardId(this.card.getId());

        this.loaded = true;
    }

    /**
     * Draw a chart with the consume of this {@link Card} grouped by {@link CostCenter}
     */
    private void loadConsumeChart() {

        final List<CardConsume> cardConsumes = this.cardConsumeRepository.findByCardId(this.card.getId());

        final BigDecimal total = cardConsumes.stream()
                .map(CardConsume::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final PieChartDataset dataset = new PieChartDataset("default");

        cardConsumes.forEach(consume -> {
            dataset.addColor(consume.getCostCenterColor());
            dataset.addData(percentageOf(consume.getValue(), total, true));
        });

        final PieChartModel model = new PieChartModel();

        model.addData(dataset);
        model.addAllLabel(cardConsumes.stream()
                .map(CardConsume::getCostCenter)
                .collect(Collectors.toList()));

        this.executeScript("drawPieChart(" + model.toJson() + ", 'consumeByCostCenterChart')");
    }

    /**
     * Draw the chart about last six {@link CreditCardInvoice} values
     */
    private void loadValuesChart() {

        final Color blue = new Color(30, 144, 255);

        final LineChartDataset<BigDecimal> valuesDataset = new LineChartDataset<>();

        valuesDataset.setLabel(MessageSource.get("card-statistics.chart.value"));
        valuesDataset.setBorderColor(blue.toString());
        valuesDataset.setBackgroundColor(blue.transparent().toString());
        valuesDataset.addAllData(this.invoices.stream()
                .map(CreditCardInvoice::getTotalValue)
                .collect(Collectors.toList()));

        final LineChartModel<BigDecimal> model = new LineChartModel<>();

        model.addDataset(valuesDataset);
        model.addAllLabels(this.invoices.stream()
                .map(invoice -> invoice.getFinancialPeriod().getIdentification())
                .collect(Collectors.toList()));

        this.executeScript("drawLineChart(" + model.toJson() + ", 'invoiceValuesChart')");
    }
}
