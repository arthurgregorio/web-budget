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
package br.com.webbudget.domain.services.misc;

import br.com.webbudget.domain.entities.financial.MovementState;
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.financial.MovementType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

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
    private List<FinancialPeriod> periods;

    private Optional<MovementType> movementType;
    private Optional<MovementState> movementStateType;
    private Optional<MovementClassType> movementClassType;

    /**
     *
     */
    public MovementFilter() {
        this.movementType = Optional.empty();
        this.movementStateType = Optional.empty();
        this.movementClassType = Optional.empty();
    }

    /**
     * @return se existe ou nao uma criteria para este filtro
     */
    public boolean hasCriteria() {
        return StringUtils.isNotBlank(this.criteria);
    }

    /**
     * @return os filtros customizados
     */
    public Criterion[] getCustomFilters() {

        final List<Criterion> custom = new ArrayList<>();

        custom.add(this.getMovementTypeCriterion());
        custom.add(this.getMovementStateTypeCriterion());
        custom.add(this.getMovementClassTypeCriterion());
        
        final Criterion periodsCriterion = this.getPeriodsCriterion();
        
        if (periodsCriterion != null) {
            custom.add(periodsCriterion);
        }

        return custom.stream()
                .filter(criterion -> criterion != null)
                .collect(Collectors.toList())
                .stream()
                .toArray(Criterion[]::new);
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

    /**
     * @return
     */
    private Criterion getMovementTypeCriterion() {
        return this.movementType.isPresent() 
                ? Restrictions.eq("movementType", this.movementType.get()) 
                : null;
    }
    
    /**
     * @return
     */
    private Criterion getMovementClassTypeCriterion() {
        return this.movementClassType.isPresent() 
                ? Restrictions.eq("mc.movementClassType", this.movementClassType.get()) 
                : null;
    }
    
    /**
     * @return
     */
    private Criterion getMovementStateTypeCriterion() {
        return this.movementStateType.isPresent() 
                ? Restrictions.eq("movementStateType", this.movementStateType.get()) 
                : null;
    }
    
    /**
     * @return 
     */
    private Criterion getPeriodsCriterion() {
        
        final Object[] values = this.periods.stream()
                .map(FinancialPeriod::getId)
                .collect(Collectors.toList())
                .stream()
                .toArray(Object[]::new);
        
        return values.length > 0 ? Restrictions.in("fp.id", values) : null;
    }

    /**
     * @return 
     */
    public MovementType getMovementType() {
        return this.movementType.orElse(null);
    }

    /**
     * @param movementType 
     */
    public void setMovementType(MovementType movementType) {
        this.movementType = Optional.ofNullable(movementType);
    }

    /**
     * @return 
     */
    public MovementState getMovementStateType() {
        return this.movementStateType.orElse(null);
    }

    /**
     * @param movementStateType 
     */
    public void setMovementStateType(MovementState movementStateType) {
        this.movementStateType = Optional.ofNullable(movementStateType);
    }

    /**
     * @return 
     */
    public MovementClassType getMovementClassType() {
        return this.movementClassType.orElse(null);
    }

    /**
     * @param movementClassType 
     */
    public void setMovementClassType(MovementClassType movementClassType) {
        this.movementClassType = Optional.ofNullable(movementClassType);
    }

    public void clear() {
        this.criteria = null;
        this.periods.clear();
        this.movementType = Optional.empty();
        this.movementStateType = Optional.empty();
        this.movementClassType = Optional.empty();
    }
}
