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
 * Mapeamento das authorities do sistema
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
    public final String CONFIGURATION_VIEW = "authority.configuration.view";
    @Getter
    @AuthorizationGroup("authority.configuration")
    public final String CONFIGURATION_INSERT = "authority.configuration.insert";

    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_VIEW = "authority.card.view";
    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_INSERT = "authority.card.insert";
    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_UPDATE = "authority.card.update";
    @Getter
    @AuthorizationGroup("authority.card")
    public final String CARD_DELETE = "authority.card.delete";

    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_VIEW = "authority.contact.view";
    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_INSERT = "authority.contact.insert";
    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_UPDATE = "authority.contact.update";
    @Getter
    @AuthorizationGroup("authority.contact")
    public final String CONTACT_DELETE = "authority.contact.delete";

    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_VIEW = "authority.wallet.view";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_INSERT = "authority.wallet.insert";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_UPDATE = "authority.wallet.update";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_DELETE = "authority.wallet.delete";
    @Getter
    @AuthorizationGroup("authority.wallet")
    public final String WALLET_ADJUST_BALANCE = "authority.wallet.adjust-balance";

    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_VIEW = "authority.cost-center.view";
    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_INSERT = "authority.cost-center.insert";
    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_UPDATE = "authority.cost-center.update";
    @Getter
    @AuthorizationGroup("authority.cost-center")
    public final String COST_CENTER_DELETE = "authority.cost-center.delete";

    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_VIEW = "authority.movement-class.view";
    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_INSERT = "authority.movement-class.insert";
    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_UPDATE = "authority.movement-class.update";
    @Getter
    @AuthorizationGroup("authority.movement-class")
    public final String MOVEMENT_CLASS_DELETE = "authority.movement-class.delete";

    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_VIEW = "authority.movement.view";
    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_INSERT = "authority.movement.insert";
    @Getter
    @AuthorizationGroup("authority.movement")
    public final String MOVEMENT_UPDATE = "authority.movement.update";
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
    public final String CARD_INVOICE_VIEW = "authority.card-invoice.view";
    @Getter
    @AuthorizationGroup("authority.card-invoice")
    public final String CARD_INVOICE_PROCESS = "authority.card-invoice.process";
    @Getter
    @AuthorizationGroup("authority.card-invoice")
    public final String CARD_INVOICE_HISTORY = "authority.card-invoice.history";

    @Getter
    @AuthorizationGroup("authority.balance-transfer")
    public final String BALANCE_TRANSFER_VIEW = "authority.balance-transfer.view";
    @Getter
    @AuthorizationGroup("authority.balance-transfer")
    public final String BALANCE_TRANSFER_MAKE = "authority.balance-transfer.make";

    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_VIEW = "authority.financial-period.view";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_INSERT = "authority.financial-period.insert";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_DELETE = "authority.financial-period.delete";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_CLOSE = "authority.financial-period.close";
    @Getter
    @AuthorizationGroup("authority.financial-period")
    public final String FINANCIAL_PERIOD_DETAILS = "authority.financial-period.details";

    @Getter
    @AuthorizationGroup("authority.closing")
    public final String CLOSING_VIEW = "authority.closing.view";
    @Getter
    @AuthorizationGroup("authority.closing")
    public final String CLOSING_CLOSE = "authority.closing.close";
    @Getter
    @AuthorizationGroup("authority.closing")
    public final String CLOSING_PROCESS = "authority.closing.process";

    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_VIEW = "authority.user.view";
    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_INSERT = "authority.user.insert";
    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_UPDATE = "authority.user.update";
    @Getter
    @AuthorizationGroup("authority.user")
    public final String USER_DELETE = "authority.user.delete";
    
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_VIEW = "authority.group.view";
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_INSERT = "authority.group.insert";
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_UPDATE = "authority.group.update";
    @Getter
    @AuthorizationGroup("authority.group")
    public final String GROUP_DELETE = "authority.group.delete";

    @Getter
    @AuthorizationGroup("authority.private-message")
    public final String PRIVATE_MESSAGE_VIEW = "authority.private-message.view";
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
        final Set<String> allAuthorities = this.listAuthorizations();

        final Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(AuthorizationGroup.class)) {

                final String group = field.getAnnotation(AuthorizationGroup.class).value();

                if (!authorities.containsKey(group)) {

                    final Set<String> grouped = new HashSet<>();

                    allAuthorities.stream().filter((authority)
                            -> (authority.contains(group))).forEach((authority) -> {
                                grouped.add(authority);
                            });
                    authorities.put(group, grouped);
                }
            }
        }
        return authorities;
    }
}
