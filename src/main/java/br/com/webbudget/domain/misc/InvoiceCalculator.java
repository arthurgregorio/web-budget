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
package br.com.webbudget.domain.misc;

import br.com.webbudget.domain.entities.entries.Card;
import br.com.webbudget.domain.entities.entries.CardInvoice;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Calculadora das estatisticas das faturas de cartao
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.1, 04/05/2016
 */
public class InvoiceCalculator {
    
    @Getter
    private final Card card;
    
    @Getter
    private final List<CardInvoice> cardInvoices;

    /**
     * 
     * @param card
     * @param cardInvoices 
     */
    public InvoiceCalculator(Card card, List<CardInvoice> cardInvoices) {
        this.card = card;
        this.cardInvoices = cardInvoices;
    }
    
    /**
     * @return o total das faturas deste cartao
     */
    public BigDecimal getInvoicesTotal() {
        return this.cardInvoices.stream()
                .map(CardInvoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * @return o valor mais baixo entre todas as faturas
     */
    public BigDecimal getLowerTotal() {
        return this.cardInvoices.stream()
                .sorted((i1, i2) -> i1.getTotal().compareTo(i2.getTotal()))
                .map(CardInvoice::getTotal)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
    
    /**
     * @return o valor mais alto entre todas as faturas
     */
    public BigDecimal getHigherTotal() {
        return this.cardInvoices.stream()
                .sorted((i1, i2) -> i2.getTotal().compareTo(i1.getTotal()))
                .map(CardInvoice::getTotal)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
    
    /**
     * @return o valor mais alto entre todas as faturas
     */
    public BigDecimal getLastTotal() {
        return this.cardInvoices.stream()
                .sorted((i1, i2) -> i2.getInclusion().compareTo(i1.getInclusion()))
                .map(CardInvoice::getTotal)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
    
    /**
     * @return as faturas que estao nesta calculadora ordenadas por inclusao
     */
    public List<CardInvoice> getOrderedByInclusion() {
        return this.cardInvoices.stream()
                .sorted((i1, i2) -> i1.getInclusion().compareTo(i2.getInclusion()))
                .collect(Collectors.toList());
    }
}