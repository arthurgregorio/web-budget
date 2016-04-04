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
package br.com.webbudget.domain.misc;

import br.com.webbudget.domain.model.entity.card.CardType;
import br.com.webbudget.domain.model.entity.movement.FinancialPeriod;
import br.com.webbudget.domain.model.entity.movement.Movement;
import br.com.webbudget.domain.model.entity.movement.MovementClass;
import br.com.webbudget.domain.model.entity.movement.MovementClassType;
import br.com.webbudget.domain.model.entity.movement.MovementType;
import br.com.webbudget.domain.model.repository.movement.IMovementClassRepository;
import br.com.webbudget.domain.model.repository.movement.IMovementRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Calculadora de movimentos, utilizada para dar melhor agilidade no calculo dos
 * valores de movimentacao financeira dentro da aplicacao.
 * 
 * Nao e um servico, ou seja, nao executa operacoes de escrita no banco, apenas 
 * leitura. Assim, todos os seus metodos sao apenas readOnly
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 22/02/2015
 */
@RequestScoped
public class MovementsCalculator1 {

    @Inject
    private IMovementRepository movementRepository;
    @Inject
    private IMovementClassRepository movementClassRepository;

    /**
     * Calcula o total de movimentos contidos em cada classe de movimento
     *
     * @param period o periodo
     * @param direction a direcao da classe, se e entrada ou saida
     *
     * @return as classes com sesus valores de movimentos do periodo
     */
    @Transactional
    public List<MovementClass> calculateTotalByMovementClass(FinancialPeriod period, MovementClassType direction) {

        final List<MovementClass> classes
                = this.movementClassRepository.listByTypeAndStatus(direction, false);

        final List<MovementClass> onlyValidClasses = new ArrayList<>();

        classes.stream().forEach((movementClass) -> {
            final BigDecimal total = this.movementRepository
                    .countTotalByPeriodAndMovementClass(period, movementClass);
            if (total != null) {
                movementClass.setTotalMovements(total);
                onlyValidClasses.add(movementClass);
            }
        });

        return onlyValidClasses;
    }

    /**
     * Calcula o total dos movimentos de mesmo tipo
     *
     * @param period o periodo de busca
     * @param direction a direcao do movimento (receita ou despesa)
     *
     * @return o total para aquele tipo
     */
    @Transactional
    public BigDecimal calculateTotalByDirection(FinancialPeriod period,
            MovementClassType direction) {

        final List<Movement> movements = this.movementRepository
                .listByPeriodAndDirection(period, direction);

        BigDecimal total = BigDecimal.ZERO;

        for (Movement movement : movements) {
            if (movement.getMovementType() == MovementType.MOVEMENT) {
                total = total.add(movement.getValue());
            }
        }

        return total;
    }

    /**
     * Lista os movimentos do perido totalizando os cosnsumos em cartao de
     * debito e credito
     *
     * @param period o periodo a pesquisar
     * @param type o tipo do cartao
     *
     * @return o total de consumo para aquele tipo
     */
    @Transactional
    public BigDecimal calculateCardExpenses(FinancialPeriod period, CardType type) {

        final List<Movement> movements = this.movementRepository
                .listByPeriodAndCardType(period, type);

        BigDecimal total = BigDecimal.ZERO;

        for (Movement movement : movements) {
            total = total.add(movement.getValue());
        }

        return total;
    }
}
