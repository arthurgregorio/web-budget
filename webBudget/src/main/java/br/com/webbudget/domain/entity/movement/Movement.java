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

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.entity.users.Contact;
import br.com.webbudget.domain.entity.PersistentEntity;
import br.com.webbudget.domain.entity.card.CardInvoice;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 04/03/2014
 */
@Entity
@Table(name = "movements")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Movement extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "{movement.value}")
    @Column(name = "value", nullable = false, length = 8)
    private BigDecimal value;
    @Getter
    @Setter
    @NotEmpty(message = "{movement.description}")
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    @Getter
    @Setter
    @NotNull(message = "{movement.due-date}")
    @Temporal(TemporalType.DATE)
    @Column(name = "due_date", nullable = false)
    private Date dueDate;
    @Getter
    @Setter
    @Enumerated
    @Column(name = "movement_state_type", nullable = false)
    private MovementStateType movementStateType;
    @Getter
    @Setter
    @Enumerated
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;
    @Getter
    @Setter
    @Column(name = "card_invoice_paid")
    private boolean cardInvoicePaid;

    @Getter
    @Setter
    @OneToOne(cascade = REMOVE)
    @JoinColumn(name = "id_payment")
    private Payment payment;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_contact")
    private Contact contact;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_card_invoice")
    private CardInvoice cardInvoice;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{movement.financial-period}")
    @JoinColumn(name = "id_financial_period", nullable = false)
    private FinancialPeriod financialPeriod;

    /**
     * Fetch eager pois sempre que precisarmos pesquisar um movimento, vamos
     * precisar saber como ele foi distribuido, ou seja, precisaremos do rateio
     */
    @Getter
    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "movement", fetch = EAGER, cascade = REMOVE)
    private List<Apportionment> apportionments;

    @Getter
    @Setter
    @Transient
    private boolean delete;
    @Getter
    @Setter
    @Transient
    private boolean transfer;

    @Getter
    @Transient
    private final List<Apportionment> deletedApportionments;

    /**
     *
     */
    public Movement() {

        this.code = this.createMovementCode();

        this.apportionments = new ArrayList<>();
        this.deletedApportionments = new ArrayList<>();

        this.cardInvoicePaid = false;
        this.movementType = MovementType.MOVEMENT;
        this.movementStateType = MovementStateType.OPEN;
    }

    /**
     *
     * @return
     */
    private String createMovementCode() {

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
            throw new ApplicationException("movement.validate.apportionment-duplicated");
        }

        // checa se nao esta inserindo outro para o mesmo CC e MC
        for (Apportionment a : this.apportionments) {
            if (a.getCostCenter().equals(apportionment.getCostCenter())
                    && a.getMovementClass().equals(apportionment.getMovementClass())) {
                throw new ApplicationException("movement.validate.apportionment-duplicated");
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
                throw new ApplicationException("movement.validate.apportionment-debit-credit");
            }
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

        BigDecimal total = BigDecimal.ZERO;

        for (Apportionment apportionment : this.apportionments) {
            total = total.add(apportionment.getValue());
        }

        return total;
    }

    /**
     * @return false se o valor dos rateios for menor ou maior que o do
     * movimento
     */
    public boolean isApportionmentsValid() {

        final BigDecimal total = this.getApportionmentsTotal();

        return total.compareTo(this.value) == 0;
    }

    /**
     * @return a diferenca entre o valor dos produtos e o valor do movimento
     */
    public BigDecimal getApportionmentsDifference() {
        return this.getApportionmentsTotal().subtract(this.value);
    }

    /**
     *
     * @return
     */
    public boolean isEditable() {
        return (this.movementStateType == MovementStateType.OPEN
                && !this.financialPeriod.isClosed());
    }

    /**
     *
     * @return
     */
    public boolean isPayable() {
        return (this.movementStateType == MovementStateType.OPEN
                && !this.financialPeriod.isClosed());
    }

    /**
     *
     * @return
     */
    public boolean isDeletable() {
        return ((this.movementStateType == MovementStateType.OPEN
                || this.movementStateType == MovementStateType.PAID)
                && !this.financialPeriod.isClosed());
    }

    /**
     *
     * @return
     */
    public boolean isOverdue() {

        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return this.dueDate.compareTo(calendar.getTime()) < 0;
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
