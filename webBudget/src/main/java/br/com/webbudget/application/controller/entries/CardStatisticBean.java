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
package br.com.webbudget.application.controller.entries;

import br.com.webbudget.application.component.chart.line.LineChartDatasetBuilder;
import br.com.webbudget.application.component.chart.line.LineChartModel;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.misc.InvoiceCalculator;
import br.com.webbudget.domain.model.entity.entries.Card;
import br.com.webbudget.domain.model.entity.entries.CardInvoice;
import br.com.webbudget.domain.model.service.CardService;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Controlle para a view de estatisticas do cartao
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.1, 06/05/2016
 */
@Named
@RequestScoped
public class CardStatisticBean extends AbstractBean {

    @Getter
    private Card card;

    @Getter
    private InvoiceCalculator calculator;

    @Inject
    private CardService cardService;

    /**
     * Inicializa a tela de estatisticas do cartao
     *
     * @param cardId o cartao que desejamos ver as estatisticas
     */
    public void initialize(long cardId) {

        // busca o cartao
        this.card = this.cardService.findCardById(cardId);

        // lista as ultimas 12 faturas do cartao selecionado
        final List<CardInvoice> invoices
                = this.cardService.listInvoicesByCard(this.card);

        this.calculator = new InvoiceCalculator(this.card, invoices);

        // monta o grafico
        this.buildInvoicesGraph();
    }

    /**
     * Monta o grafico do historico de valor das faturas
     */
    private void buildInvoicesGraph() {

        final LineChartDatasetBuilder<BigDecimal> datasetBuilder
                = new LineChartDatasetBuilder<>()
                .filledByColor("rgba(255, 153, 0, 0.3)")
                .withStrokeColor("rgb(255, 102, 0)")
                .withPointColor("rgb(255, 51, 0)")
                .withPointStrokeColor("#fff")
                .withPointHighlightFillColor("#fff")
                .withPointHighlightStroke("rgb(255, 102, 0)");

        final LineChartModel chartModel = new LineChartModel();

        // pega a lista de faturas
        List<CardInvoice> invoices
                = this.calculator.getOrderedByInclusion();

        // secciona a lista para pegar somente as 12 primeiras
        invoices = invoices.subList(0, 
                invoices.size() >= 6 ? 6 : invoices.size());
        
        // coloca o nome das series e os dados
        invoices.stream()
                .forEach(invoice -> {
                    chartModel.addLabel(invoice.getIdentification());
                    datasetBuilder.andData(invoice.getTotal());
                });

        chartModel.addDataset(datasetBuilder.build());

        // desenha
        this.drawLineChart("invoiceHistoric", chartModel);
    }
}
