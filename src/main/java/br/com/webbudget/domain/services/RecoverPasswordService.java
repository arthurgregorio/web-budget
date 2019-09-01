/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.services;

import br.com.webbudget.domain.entities.configuration.StoreType;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.domain.repositories.configuration.UserRepository;
import br.com.webbudget.infrastructure.mail.*;
import br.com.webbudget.infrastructure.utils.Configurations;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import br.com.webbudget.infrastructure.utils.RandomCode;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service used to run the process of password recovering
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 02/04/2018
 */
@ApplicationScoped
public class RecoverPasswordService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private Event<MailMessage> mailSender;

    /**
     * Recover the password and send a e-mail to the user
     *
     * @param email the e-mail address of the user to recover and notify
     */
    @Transactional
    public void recover(String email) {

        final User user = this.userRepository
                .findByEmailAndStoreType(email, StoreType.LOCAL)
                .orElseThrow(() -> new BusinessLogicException("error.recover-password.user-not-found"));

        final String newPassword = RandomCode.alphanumeric(8);

        user.setPassword(this.passwordEncoder.encryptPassword(newPassword));

        this.userRepository.saveAndFlushAndRefresh(user);

        final MailMessage mailMessage = SimpleMailMessage.getBuilder()
                .from(Configurations.get("email.no-reply-address"), "webBudget")
                .to(user.getEmail())
                .withTitle(MessageSource.get("recover-password.email.title"))
                .withContent(this.buildContent(user, newPassword))
                .build();

        this.mailSender.fire(mailMessage);
    }

    /**
     * Construct the content of the e-mail message
     *
     * @param user the user
     * @param newPassword the new password
     * @return the content provider with the content
     */
    private MailContentProvider buildContent(User user, String newPassword) {

        final MustacheProvider provider = new MustacheProvider("recoverPassword.mustache");

        provider.addContent("translate", MailUtils.translateFunction());
        provider.addContent("user", user.getName());
        provider.addContent("newPassword", newPassword);

        return provider;
    }
}