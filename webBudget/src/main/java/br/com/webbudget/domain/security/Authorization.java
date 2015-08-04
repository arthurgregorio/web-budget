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
package br.com.webbudget.domain.security;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import lombok.Getter;

/**
 * Mapeamento das permissoes individuais do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 24/06/2014
 */
@Named
@Dependent
public class Authorization {

    @Getter
    @AuthorizationGroup("authority.configuration")
    public final String CONFIGURATION_VIEW = "authority.configuration.access";
    @Getter
    @AuthorizationGroup("authority.configuration")
    public final String CONFIGURATION_INSERT = "authority.configuration.add";

    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_VIEW = "authority.card.access";
    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_INSERT = "authority.card.add";
    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_UPDATE = "authority.card.edit";
    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_DELETE = "authority.card.delete";

    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_VIEW = "authority.contact.access";
    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_INSERT = "authority.contact.add";
    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_UPDATE = "authority.contact.edit";
    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_DELETE = "authority.contact.delete";

    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_VIEW = "authority.wallet.access";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_INSERT = "authority.wallet.add";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_UPDATE = "authority.wallet.edit";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_DELETE = "authority.wallet.delete";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_ADJUST_BALANCE = "authority.wallet.adjust-balance";

    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_VIEW = "authority.cost-center.access";
    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_INSERT = "authority.cost-center.add";
    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_UPDATE = "authority.cost-center.edit";
    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_DELETE = "authority.cost-center.delete";

    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_VIEW = "authority.movement-class.access";
    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_INSERT = "authority.movement-class.add";
    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_UPDATE = "authority.movement-class.edit";
    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_DELETE = "authority.movement-class.delete";

    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_VIEW = "authority.movement.access";
    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_INSERT = "authority.movement.add";
    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_UPDATE = "authority.movement.edit";
    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_PAY = "authority.movement.pay";
    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_DELETE = "authority.movement.delete";

    @Getter
    @AuthorizationGroup("authority.card-invoice")
    public final String CARD_INVOICE_PAY = "authority.card-invoice.pay";
    @Getter
    @AuthorizationGroup("authority.card-invoice")
    public final String CARD_INVOICE_VIEW = "authority.card-invoice.access";
    @Getter
    @AuthorizationGroup("authority.card-invoice")
    public final String CARD_INVOICE_PROCESS = "authority.card-invoice.process";
    @Getter
    @AuthorizationGroup("authority.card-invoice")
    public final String CARD_INVOICE_HISTORY = "authority.card-invoice.history";

    @Getter
    @AuthorizationGroup("authority.balance-transfer")
    public final String BALANCE_TRANSFER_VIEW = "authority.balance-transfer.access";
    @Getter
    @AuthorizationGroup("authority.balance-transfer")
    public final String BALANCE_TRANSFER_MAKE = "authority.balance-transfer.make";

    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_VIEW = "authority.financial-period.access";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_INSERT = "authority.financial-period.add";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_DELETE = "authority.financial-period.delete";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_DETAILS = "authority.financial-period.details";

    @Getter
    @AuthorizationGroup("authority.closing")
    public final String CLOSING_VIEW = "authority.closing.access";
    @Getter
    @AuthorizationGroup("authority.closing")
    public final String CLOSING_CLOSE = "authority.closing.close";
    @Getter
    @AuthorizationGroup("authority.closing")
    public final String CLOSING_PROCESS = "authority.closing.process";

    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_VIEW = "authority.user.access";
    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_INSERT = "authority.user.add";
    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_UPDATE = "authority.user.edit";
    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_DELETE = "authority.user.delete";
    
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_VIEW = "authority.group.access";
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_INSERT = "authority.group.add";
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_UPDATE = "authority.group.edit";
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_DELETE = "authority.group.delete";

    @Getter
    @AuthorizationGroup("authority.private-message")
    public final String PRIVATE_MESSAGE_VIEW = "authority.private-message.access";
    @Getter
    @AuthorizationGroup("authority.private-message")
    public final String PRIVATE_MESSAGE_SEND = "authority.private-message.send";

    /**
     * Lista todas as authorities disponiveis para uso, este metodo e utilzado
     * para criar o admin no bootstrap da aplicacao
     *
     * @return um set com todas as authorities disponiveis
     */
    public Set<String> listAuthorizations() {

        final Set<String> authorities = new HashSet<>();

        final Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            // verifica se a permissao tem grupo de permisao
            if (field.isAnnotationPresent(AuthorizationGroup.class)) {

                // adiciona as permissoes especificas
                try {
                    authorities.add((String) field.get(Authorization.this));
                } catch (IllegalAccessException ex) { }
            }
        }
        return authorities;
    }

    /**
     * Lista todas as authorities agrupadas pelo grupo de cada uma
     *
     * @return hashmap com os valores: grupo e itens do grupo
     */
    public HashMap<String, Set<String>> listGroupedAuthorizations() {

        final HashMap<String, Set<String>> authorities = new HashMap<>();
        final Set<String> authorizations = this.listAuthorizations();

        final Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(AuthorizationGroup.class)) {

                final String group = field.getAnnotation(AuthorizationGroup.class).value();

                if (!authorities.containsKey(group)) {

                    final Set<String> grouped = new HashSet<>();

                    authorizations.stream().filter((authorization)
                            -> (authorization.contains(group))).forEach((key) -> {
                                grouped.add(key);
                            });
                    authorities.put(group, grouped);
                }
            }
        }
        return authorities;
    }
}