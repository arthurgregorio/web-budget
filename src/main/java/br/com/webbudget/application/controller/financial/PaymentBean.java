/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.application.components.ui.ViewState;
import br.com.webbudget.domain.entities.financial.Payment;
import br.com.webbudget.domain.entities.financial.PaymentMethod;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.entities.registration.Wallet;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.repositories.registration.CardRepository;
import br.com.webbudget.domain.repositories.registration.WalletRepository;
import br.com.webbudget.domain.services.PaymentService;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * The {@link Payment} view controller
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 23/02/2019
 */
@Named
@ViewScoped
public class PaymentBean extends AbstractBean {

    @Getter
    private ViewState viewState;

    @Getter
    private Payment payment;
    @Getter
    private PeriodMovement periodMovement;

    @Getter
    private List<Wallet> wallets;
    @Getter
    private List<Card> debitCards;
    @Getter
    private List<Card> creditCards;

    @Inject
    private CardRepository cardRepository;
    @Inject
    private WalletRepository walletRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;

    @Inject
    private PaymentService paymentService;

    /**
     * Initialize the bean to process the payment
     *
     * @param id the {@link PeriodMovement} to be paid
     * @param viewState the {@link ViewState} to be used
     */
    public void initialize(long id, ViewState viewState) {
        this.viewState = viewState;

        this.payment = new Payment();

        this.wallets = this.walletRepository.findAllActive();
        this.debitCards = this.cardRepository.findByCardTypeAndActive(CardType.DEBIT, true);
        this.creditCards = this.cardRepository.findByCardTypeAndActive(CardType.CREDIT, true);

        this.periodMovement = this.periodMovementRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("error.payment.cant-find-movement"));
    }

    /**
     * Pay the {@link PeriodMovement} and show a a dialog to confirm the operation
     */
    public void doPayment() {
        this.paymentService.pay(this.periodMovement, this.payment);
        this.updateAndOpenDialog("paymentConfirmationDialog", "dialogPaymentConfirmation");
    }

    /**
     * Pay the {@link PeriodMovement} and put a message in the flash scope to be displayed on the form after the redirect
     *
     * @return the outcome to the {@link PeriodMovement} form
     */
    public String doPaymentAndNew() {
        this.paymentService.pay(this.periodMovement, this.payment);
        this.addInfoAndKeep("info.payment.success", this.periodMovement.getIdentification());
        return this.changeToAdd();
    }

    /**
     * Go back to the list of {@link PeriodMovement}
     *
     * @return outcome to {@link PeriodMovement} listing
     */
    public String changeToListing() {
        return "listPeriodMovements.xhtml?faces-redirect=true";
    }

    /**
     * Change back to the {@link PeriodMovement} form
     *
     * @return outcome to {@link PeriodMovement} form
     */
    public String changeToAdd() {
        return "formPeriodMovement.xhtml?faces-redirect=true&viewState=ADDING";
    }

    /**
     * Get all possible {@link PaymentMethod}
     *
     * @return all available {@link PaymentMethod}
     */
    public PaymentMethod[] getPaymentMethods() {
        return PaymentMethod.values();
    }
}
