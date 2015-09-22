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
package br.com.webbudget.domain.entity.movement;

import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.misc.ex.WbDomainException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A entidade que representa nosso movimento fixo
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 18/09/2015
 */
@Entity
@Table(name = "fixed_movements")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class FixedMovement extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotEmpty(message = "{fixed-movement.identification}")
    @Column(name = "description", nullable = false, length = 45)
    private String identification;
    @Getter
    @Setter
    @NotNull(message = "{fixed-movement.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Getter
    @Setter
    @Column(name = "quotes")
    private int quotes;

    @Getter
    @Setter
    @Enumerated
    @Column(name = "fixed_movement_type", nullable = false)
    private FixedMovementType fixedMovementType;
    @Getter
    @Setter
    @Enumerated
    @Column(name = "fixed_movement_status_type", nullable = false)
    private FixedMovementStatusType fixedMovementStatusType;

    /**
     * Fetch eager pois sempre que precisarmos pesquisar um movimento, vamos
     * precisar saber como ele foi distribuido, ou seja, precisaremos do rateio
     */
    @Getter
    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "fixedMovement", fetch = EAGER, cascade = REMOVE)
    private List<Apportionment> apportionments;

    @Getter
    @Transient
    private final List<Apportionment> deletedApportionments;

    /**
     * Inicializamos o que for necessario
     */
    public FixedMovement() {
        
        this.code = this.createFixedMovementCode();
        
        this.fixedMovementType = FixedMovementType.FIXED;
        
        this.apportionments = new ArrayList<>();
        this.deletedApportionments = new ArrayList<>();
    }

    /**
     * @return o codigo unico gerado por marca de tempo para esta entidade
     */
    private String createFixedMovementCode() {

        long decimalNumber = System.nanoTime();

        String generated = "";
        final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        synchronized (this.getClass()) {

            int mod;
            int authCodeLength = 0;

            while (decimalNumber != 0 && authCodeLength < 5) {

                mod = (int) (decimalNumber % 36);
                generated = digits.substring(mod, mod + 1) + generated;
                decimalNumber = decimalNumber / 36;
                authCodeLength++;
            }
        }
        return generated;
    }

    /**
     *
     * @param apportionment
     */
    public void addApportionment(Apportionment apportionment) {

        // checa se nao esta sendo inserido outro exatamente igual
        if (this.apportionments.contains(apportionment)) {
            throw new WbDomainException("fixed-movement.validate.apportionment-duplicated");
        }

        // checa se nao esta inserindo outro para o mesmo CC e MC
        for (Apportionment a : this.apportionments) {
            if (a.getCostCenter().equals(apportionment.getCostCenter())
                    && a.getMovementClass().equals(apportionment.getMovementClass())) {
                throw new WbDomainException("fixed-movement.validate.apportionment-duplicated");
            }
        }

        // verificamos se os movimentos partem na mesma direcao, para que nao
        // haja rateios com debitos e creditos juntos
        if (!this.apportionments.isEmpty()) {

            final MovementClassType direction = this.getDirection();
            final MovementClassType apportionmentDirection
                    = apportionment.getMovementClass().getMovementClassType();

            if ((direction == MovementClassType.IN && apportionmentDirection == MovementClassType.OUT)
                    || (direction == MovementClassType.OUT && apportionmentDirection == MovementClassType.IN)) {
                throw new WbDomainException("fixed-movement.validate.apportionment-debit-credit");
            }
        }

        // impossivel ter um rateio com valor igual a zero
        if (apportionment.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new WbDomainException("fixed-movement.validate.apportionment-invalid-value");
        }

        // impossivel ter um rateio com valor maior que o do movimento
        if (apportionment.getValue().compareTo(this.value) > 0) {
            throw new WbDomainException("fixed-movement.validate.apportionment-invalid-value");
        }

        this.apportionments.add(apportionment);
    }

    /**
     *
     * @param id
     */
    public void removeApportionment(String id) {

        Apportionment toRemove = null;

        for (Apportionment apportionment : this.apportionments) {
            if (id.equals(apportionment.getCode())) {
                toRemove = apportionment;
            }
        }

        this.apportionments.remove(toRemove);
        this.addDeletedApportionment(toRemove);
    }

    /**
     *
     * @param apportionment
     */
    public void removeApportionment(Apportionment apportionment) {
        this.apportionments.remove(apportionment);
        this.addDeletedApportionment(apportionment);
    }

    /**
     * Usado em caso de um rateio jah persistente ser apagado, ele precisara ser
     * removido do banco tambem
     *
     * @param apportionment o rateio removido da lista que sera colocado na
     * lista de deletados somente se ele ja houve sido persistido
     */
    private void addDeletedApportionment(Apportionment apportionment) {
        if (apportionment.isSaved()) {
            this.deletedApportionments.add(apportionment);
        }
    }

    /**
     * @return o valor da somatoria dos rateios
     */
    public BigDecimal getApportionmentsTotal() {

        final BigDecimal total = this.apportionments
                .stream()
                .map(Apportionment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total;
    }

    /**
     * @return o valor a ser rateado descontando o valor ja rateado
     */
    public BigDecimal getValueToDivide() {
        return this.value.subtract(this.getApportionmentsTotal());
    }

    /**
     * @return false se o valor dos rateios for menor ou maior que o do
     * movimento
     */
    public boolean isApportionmentsValid() {
        return this.getApportionmentsTotal().compareTo(this.value) == 0;
    }

    /**
     * @return a diferenca entre o valor dos produtos e o valor do movimento
     */
    public BigDecimal getApportionmentsDifference() {
        return this.getApportionmentsTotal().subtract(this.value);
    }

    /**
     * De acordo com a primeira classe do rateio, diz se o movimento e de
     * entrada ou saida
     *
     * @return a direcao do movimento de acordo com as classes usadas
     */
    public MovementClassType getDirection() {
        for (Apportionment apportionment : this.apportionments) {
            return apportionment.getMovementClass().getMovementClassType();
        }
        return null;
    }
}
