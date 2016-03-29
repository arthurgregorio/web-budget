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

import br.com.webbudget.domain.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.entity.movement.MovementClassType;
import br.com.webbudget.domain.entity.movement.MovementStateType;
import br.com.webbudget.domain.entity.movement.MovementType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe que constroi o filtro para a nossa pesquisa de movimentos
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 14/09/2015
 */
@ToString
public final class MovementFilter {

    @Getter
    @Setter
    private String criteria;

    @Getter
    @Setter
    private MovementType movementType;
    @Getter
    @Setter
    private FinancialPeriod financialPeriod;
    @Getter
    @Setter
    private MovementStateType movementStateType;
    @Getter
    @Setter
    private MovementClassType movementClassType;

    /**
     * @return se existe ou nao uma criteria para este filtro
     */
    public boolean hasCriteria() {
        return StringUtils.isNotBlank(this.criteria);
    }

    /**
     * Metodo para fazer o parse da nossa criteria em um numero decimal para
     * satisfazer a busca por valor
     * 
     * @return o valor formatador em bigdecimal
     * 
     * @throws ParseException se houver algum erro na hora do parse
     */
    public BigDecimal criteriaToBigDecimal() throws ParseException {

        final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0#", symbols);
        decimalFormat.setParseBigDecimal(true);

        return (BigDecimal) decimalFormat.parse(this.criteria);
    }
}
