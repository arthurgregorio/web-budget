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
package br.com.webbudget.domain.misc.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO simples para transitar os dados do filtro de movimentos entre a view e 
 * a camada de servicos
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 14/09/2015
 */
@ToString
public class MovementFilter {

    @Getter
    @Setter
    private String criteria;
    @Getter
    @Setter
    private Boolean onlyPaid;
    @Getter
    @Setter
    private Boolean onlyOpenPeriods;

    /**
     * Inicializamos o que for necessario
     */
    public MovementFilter() {
        this.onlyOpenPeriods = Boolean.TRUE;
    }
    
    /**
     * @param criteria os filtros possiveis de serem aplicados e castados para 
     * string
     * @return este builder
     */
    public MovementFilter onlyOpenPeriods(String criteria) {
        this.criteria = criteria;
        return this;
    }
    
    /**
     * @param onlyPaid se deve ou nao filtrar apenas movimentos pagos no sistema
     * @return este builder
     */
    public MovementFilter onlyPaid(Boolean onlyPaid) {
        this.onlyPaid = onlyPaid;
        return this;
    }
    
    /**
     * @param onlyOpenPeriods se devemos ou nao listar apenas pelos periodos em
     * aberto no sistema
     * @return este builder
     */
    public MovementFilter searchOnOpenPeriods(Boolean onlyOpenPeriods) {
        this.onlyOpenPeriods = onlyOpenPeriods;
        return this;
    }
    
    /**
     * @return se esta busca retorna apenas movimentos pagos
     */
    public boolean isOnlyPaidMovements() {
        return this.onlyPaid != null && this.onlyPaid.equals(Boolean.TRUE);
    }
    
    /**
     * @return se esta busca tem criterios de busca
     */
    public boolean hasCriteria() {
        return this.criteria != null && !this.criteria.isEmpty();
    }
    
    /**
     * @return indica se estamos filtrando apenas pelos periodos financeiros em 
     * aberto no sistema
     */
    public boolean isOnlyByOpenPeriods() {
        return this.onlyOpenPeriods != null && this.onlyOpenPeriods.equals(Boolean.TRUE);
    }
    
    /**
     * 
     * @return 
     */
    public boolean isOnlyPaidsByOpenPeriods() {
        return this.isOnlyByOpenPeriods() && this.isOnlyPaidMovements();
    }
}