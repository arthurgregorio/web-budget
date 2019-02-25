package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.financial.Payment;
import br.com.webbudget.domain.entities.financial.PeriodMovement;
import br.com.webbudget.domain.repositories.financial.PaymentRepository;
import br.com.webbudget.domain.repositories.financial.PeriodMovementRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * The {@link Payment} service
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 23/02/2019
 */
@ApplicationScoped
public class PaymentService {

    @Inject
    private PaymentRepository paymentRepository;
    @Inject
    private PeriodMovementRepository periodMovementRepository;

    /**
     *
     * @param periodMovement
     * @param payment
     */
    @Transactional
    public void pay(PeriodMovement periodMovement, Payment payment) {
        // TODO make the payment happen
    }
}
