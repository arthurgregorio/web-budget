package br.com.webbudget.application.producer;

import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.security.User;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.Identity;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 22/06/2015
 */
public class AuthenticatedUserProducer {

    @Inject
    private Identity identity;

    /**
     * @return o usuario logado no sistema
     */
    @Produces
    @RequestScoped
    @AuthenticatedUser
    User produceUser() {
        return (User) this.identity.getAccount();
    }
}
