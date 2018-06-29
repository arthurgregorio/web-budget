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
import br.com.webbudget.domain.entities.registration.FinancialPeriod;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClassType;
import br.com.webbudget.domain.entities.registration.Contact;
import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.CardInvoice;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 04/03/2014
 */
@Entity
@Audited
@Table(name = "movements")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Movement extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
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
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
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
    @OneToOne(mappedBy = "movement", cascade = REMOVE)
    private Launch launch;

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
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "movement", fetch = EAGER, cascade = REMOVE)
    private List<Apportionment> apportionments;

    /**
     * Atributo usado para o controle da view no momento de checar se um deter-
     * minado movimento foi ou nao conferido na fatura do cartao
     */
    @Getter
    @Setter
    @Transient
    private boolean checked;

    @Getter
    @Transient
    private final List<Apportionment> deletedApportionments;

    /**
     *
     */
    public Movement() {

        this.code = RandomCode.alphanumeric(5);

        this.apportionments = new ArrayList<>();
        this.deletedApportionments = new ArrayList<>();

        this.cardInvoicePaid = false;
        this.movementType = MovementType.MOVEMENT;
        this.movementStateType = MovementStateType.OPEN;
    }

    /**
     * Metodo para adicao de rateios ao movimento
     *
     * @param apportionment o rateio a ser adiocionado
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
        if (apportionment.getValue().compareTo(BigDecimal.ZERO) == 0
                || apportionment.getValue().compareTo(this.value) > 0) {
            throw new BusinessLogicException("error.apportionment.invalid-value");
        }

        this.apportionments.add(apportionment);
    }

    /**
     * Remove um rateio pelo seu codigo, caso nao localize o mesmo dispara uma
     * exception para informor ao usuario que nao podera fazer nada pois sera um
     * problema do sistema...
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
     * @return o valor deste movimento considerando seu possivel desconto
     */
    public BigDecimal getValue() {
        if (this.payment != null && this.payment.hasDiscount()) {
            return this.value.subtract(this.payment.getDiscount());
        } 
        return value;
    }
    
    /**
     * @return retorna o valor original deste movimento, sem descontos
     */
    public BigDecimal getOriginalValue() {
        return this.value;
    }

    /**
     * @return o nome do contato vinculado ao movimento
     */
    public String getContactName() {
        return this.contact != null ? this.contact.getName() : "";
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
     * @return se este movimento foi pago com cartao de credito
     */
    public boolean isPaidOnCreditCard() {
        return this.isExpense() && this.payment.getPaymentMethodType()
                == PaymentMethodType.CREDIT_CARD;
    }
    
    /**
     * @return se este movimento foi pago em cartao de debito
     */
    public boolean isPaidOnDebitCard() {
        return this.isExpense() && this.payment.getPaymentMethodType()
                == PaymentMethodType.DEBIT_CARD;
    }

    /**
     * @return se este movimento eh uma fatura de cartao ou nao
     */
    public boolean isCardInvoice() {
        return this.movementType == MovementType.CARD_INVOICE;
    }
    
    /**
     * @return se este movimento nao eh uma fatura de cartao
     */
    public boolean isNotCardInvoice() {
        return this.movementType != MovementType.CARD_INVOICE;
    }

    /**
     * @return se temos um movimento editavel
     */
    public boolean isEditable() {
        return (this.movementStateType == MovementStateType.OPEN
                && !this.financialPeriod.isClosed());
    }

    /**
     * @return se o movimento esta pago ou nao
     */
    public boolean isPaid() {
        return this.movementStateType == MovementStateType.PAID
                || this.movementStateType == MovementStateType.CALCULATED;
    }

    /**
     * @return se temos um movimento pagavel
     */
    public boolean isPayable() {
        return this.movementStateType == MovementStateType.OPEN
                && !this.financialPeriod.isClosed();
    }

    /**
     * @return se temos um movimento deletavel
     */
    public boolean isDeletable() {
        return (this.movementStateType == MovementStateType.OPEN
                || this.movementStateType == MovementStateType.PAID)
                && !this.financialPeriod.isClosed();
    }

    /**
     * @return se o movimento esta vencido ou nao
     */
    public boolean isOverdue() {
        return this.dueDate.isBefore(LocalDate.now());
    }

    /**
     * @return se temos ou nao nao movimento a data de pagamento setada
     */
    public boolean hasDueDate() {
        return this.dueDate != null;
    }

    /**
     * @return se este movimento e uma despesa
     */
    public boolean isExpense() {
        return this.apportionments
                .stream()
                .findFirst()
                .map(apportionment -> apportionment.isForExpenses())
                .orElse(false);
    }

    /**
     * @return se este movimento e uma receita
     */
    public boolean isRevenue() {
        return this.apportionments
                .stream()
                .findFirst()
                .map(apportionment -> apportionment.isForRevenues())
                .orElse(false);
    }

    /**
     * @return a data de pagamento deste movimento
     */
    public LocalDate getPaymentDate() {
        return this.payment.getPaymentDate();
    }

    /**
     * @return todos os centros de custo que este movimento faz parte
     */
    public List<CostCenter> getCostCenters() {

        final List<CostCenter> costCenters = new ArrayList<>();

        this.apportionments.stream().forEach((apportionment) -> {
            costCenters.add(apportionment.getMovementClass().getCostCenter());
        });

        return costCenters;
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
     * Realiza a validacao dos rateios do movimento fixo
     */
    public void validateApportionments() {

        if (this.getApportionments().isEmpty()) {
            throw new BusinessLogicException(
                    "error.apportionment.empty-apportionment");
        } else if (this.getApportionmentsTotal().compareTo(this.getValue()) > 0) {
            final String difference = String.format("%10.2f",
                    this.getApportionmentsTotal().subtract(this.getValue()));
            throw new BusinessLogicException(
                    "error.apportionment.gt-value", difference);
        } else if (this.getApportionmentsTotal().compareTo(this.getValue()) < 0) {
            final String difference = String.format("%10.2f",
                    this.getValue().subtract(this.getApportionmentsTotal()));
            throw new BusinessLogicException(
                    "error.apportionment.lt-value", difference);
        }
    }

    /**
     * @return se este movimento e o ultimo lancamento de uma serie de 
     * lancamentos de movimentos fixos
     */
    public boolean isLastLaunch() {
        return this.launch != null && this.launch.getFixedMovement().isFinalized();
    }

    /**
     * @return se este movimento tem ou nao mais de um rateio
     */
    public boolean isMultiApportionment() {
        return this.apportionments.size() > 1;
    }
    
    /**
     * Valida o pagamento deste movimento
     */
    public void validatePayment() {

        // movimentos com multiplos rateios nao sao passiveis de desconto
        if (this.payment.hasDiscount() && this.isMultiApportionment()) {
            throw new BusinessLogicException("error.payment.discount-app-multi");
        } 
        
        // valida as formas de pagamento informadas
        this.payment.validatePaymentMethod();
        
        // valida o valor do desconto
        this.payment.validateDiscount(this.value);
        
        // aplica o desconto nos rateios caso tenha
        this.apportionments.forEach(a -> {
            a.setValue(this.getValue());
        });
    }
}
