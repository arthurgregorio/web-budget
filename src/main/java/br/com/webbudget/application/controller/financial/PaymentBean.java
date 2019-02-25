package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.entities.financial.Payment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;
import br.com.webbudget.domain.services.PaymentService;
import lombok.Getter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    private Payment payment;
    @Getter
    private PeriodMovement periodMovement;

    @Inject
    private PeriodMovementRepository periodMovementRepository;

    @Inject
    private PaymentService paymentService;

    /**
     * Initialize the bean to process the payment
     *
     * @param movementId the {@link PeriodMovement} to be paid
     */
    public void initialize(long movementId) {

        this.payment = new Payment();

        this.periodMovement = this.periodMovementRepository.findById(movementId)
                .orElseThrow(() -> new BusinessLogicException("error.payment.cant-find-movement"));
    }

    /**
     * This method call the service to pay the {@link PeriodMovement} and than, go back to the listing view
     */
    public void doPayment() {
        this.paymentService.pay(this.periodMovement, this.payment);
        this.updateAndOpenDialog("paymentConfirmationDialog", "dialogPaymentConfirmation");
    }

    /**
     * Go back to the list of {@link PeriodMovement}
     *
     * @return the outcome to the list of {@link PeriodMovement}
     */
    public String changeToListing() {
        return "listPeriodMovements.xhtml?faces-redirect=true";
    }
}
