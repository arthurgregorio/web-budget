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
package br.com.webbudget.domain.entities.financial;

import br.com.webbudget.infrastructure.utils.RandomCode;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Column(name = "identification", nullable = false, length = 45)
    private String identification;
    @Getter
    @Setter
    @NotEmpty(message = "{fixed-movement.description}")
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    @Getter
    @Setter
    @NotNull(message = "{fixed-movement.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Getter
    @Setter
    @Column(name = "quotes")
    private Integer quotes;
    @Getter
    @Setter
    @Column(name = "auto_launch", nullable = false)
    private boolean autoLaunch;
    @Getter
    @Setter
    @Column(name = "undetermined", nullable = false)
    private boolean undetermined;
    @Getter
    @Setter
    @Column(name = "start_date")
    private LocalDate startDate;

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
    @Setter
    @Transient
    private boolean alreadyLaunched;

    @Getter
    @Setter
    @Transient
    private List<Launch> launches;

    @Getter
    @Transient
    private final List<Apportionment> deletedApportionments;

    /**
     * Inicializamos o que for necessario
     */
    public FixedMovement() {

        this.code = RandomCode.alphanumeric(5);

        this.autoLaunch = false;
        this.fixedMovementStatusType = FixedMovementStatusType.ACTIVE;

        this.apportionments = new ArrayList<>();
        this.deletedApportionments = new ArrayList<>();
    }

    /**
     *
     * @param apportionment
     */
    public void addApportionment(Apportionment apportionment) {

        // checa se nao esta sendo inserido outro exatamente igual
        if (this.apportionments.contains(apportionment)) {
            throw new BusinessLogicException("error.apportionment.duplicated");
        }

        // checa se nao esta inserindo outro para o mesmo CC e MC
        for (Apportionment a : this.apportionments) {
            if (a.getCostCenter().equals(apportionment.getCostCenter())
                    && a.getMovementClass().equals(apportionment.getMovementClass())) {
                throw new BusinessLogicException("error.apportionment.duplicated");
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
                throw new BusinessLogicException("error.apportionment.mix-of-classes");
            }
        }

        // impossivel ter um rateio com valor igual a zero
        if (apportionment.getValue().compareTo(BigDecimal.ZERO) == 0 || 
                apportionment.getValue().compareTo(this.value) > 0) {
            throw new BusinessLogicException("error.apportionment.invalid-value");
        }

        this.apportionments.add(apportionment);
    }

    /**
     * Remove um rateio pelo seu codigo, caso nao localize o mesmo dispara uma 
     * exception para informor ao usuario que nao podera fazer nada pois sera
     * um problema do sistema...
     * 
     * LOL WHAT!?
     *
     * @param code o codigo
     */
    public void removeApportionment(String code) {

        final Apportionment toRemove = this.apportionments.stream()
                .filter(apportionment -> apportionment.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(
                        "error.apportionment.not-found", code));

        // se o rateio ja foi salvo, adicionamos ele em outra lista 
        // para que quando salvar o movimento ele seja deletado
        if (toRemove.isSaved()) {
            this.deletedApportionments.add(toRemove);
        }

        // remove da lista principal
        this.apportionments.remove(toRemove);
    }

    /**
     * @return o valor da somatoria dos rateios
     */
    public BigDecimal getApportionmentsTotal() {
        return this.apportionments
                .stream()
                .map(Apportionment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * @return o valor a ser rateado descontando o valor ja rateado
     */
    public BigDecimal getValueToDivide() {
        return this.value.subtract(this.getApportionmentsTotal());
    }

    /**
     * @return se existe ou nao valores para serem rateados
     */
    public boolean hasValueToDivide() {
        return this.getApportionmentsTotal().compareTo(this.value) == 0;
    }
    
    /**
     * Realiza a validacao dos rateios do movimento fixo
     */
    public void validateApportionments() {

        if (this.getApportionments().isEmpty()) {
            throw new BusinessLogicException(
                    "error.apportionment.empty-apportionment");
        } else if (this.getApportionmentsTotal().compareTo(this.value) > 0) {
            final String difference = String.format("%10.2f",
                    this.getApportionmentsTotal().subtract(this.value));
            throw new BusinessLogicException(
                    "error.apportionment.gt-value", difference);
        } else if (this.getApportionmentsTotal().compareTo(this.value) < 0) {
            final String difference = String.format("%10.2f",
                    this.value.subtract(this.getApportionmentsTotal()));
            throw new BusinessLogicException(
                    "error.apportionment.lt-value", difference);
        }
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

    /**
     * @return se este movimento fixo ja finalizou ou nao
     */
    boolean isFinalized() {
        return this.fixedMovementStatusType == FixedMovementStatusType.FINALIZED;
    }
}
