package br.com.webbudget.domain.repository.movement;

import br.com.webbudget.domain.entity.movement.Payment;
import br.com.webbudget.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/10/2013
 */
@Repository
public class PaymentRepository extends GenericRepository<Payment, Long> implements IPaymentRepository {

}
