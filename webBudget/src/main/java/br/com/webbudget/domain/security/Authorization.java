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
import lombok.Getter;

/**
 * Mapeamento das authorities do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 24/06/2014
 */
public class Authorization {

    @Getter
    @AuthorizationGroup("configuration.authority")
    public final String CONFIGURATION_VIEW = "configuration.authority.view";
    @Getter
    @AuthorizationGroup("configuration.authority")
    public final String CONFIGURATION_INSERT = "configuration.authority.insert";
    
    @Getter
    @AuthorizationGroup("card.authority")
    public final String CARD_VIEW = "card.authority.view";
    @Getter
    @AuthorizationGroup("card.authority")
    public final String CARD_INSERT = "card.authority.insert";
    @Getter
    @AuthorizationGroup("card.authority")
    public final String CARD_UPDATE = "card.authority.update";
    @Getter
    @AuthorizationGroup("card.authority")
    public final String CARD_DELETE = "card.authority.delete";

    @Getter
    @AuthorizationGroup("contact.authority")
    public final String CONTACT_VIEW = "contact.authority.view";
    @Getter
    @AuthorizationGroup("contact.authority")
    public final String CONTACT_INSERT = "contact.authority.insert";
    @Getter
    @AuthorizationGroup("contact.authority")
    public final String CONTACT_UPDATE = "contact.authority.update";
    @Getter
    @AuthorizationGroup("contact.authority")
    public final String CONTACT_DELETE = "contact.authority.delete";

    @Getter
    @AuthorizationGroup("wallet.authority")
    public final String WALLET_VIEW = "wallet.authority.view";
    @Getter
    @AuthorizationGroup("wallet.authority")
    public final String WALLET_INSERT = "wallet.authority.insert";
    @Getter
    @AuthorizationGroup("wallet.authority")
    public final String WALLET_UPDATE = "wallet.authority.update";
    @Getter
    @AuthorizationGroup("wallet.authority")
    public final String WALLET_DELETE = "wallet.authority.delete";
    @Getter
    @AuthorizationGroup("wallet.authority")
    public final String WALLET_ADJUST_BALANCE = "wallet.authority.adjust-balance";

    @Getter
    @AuthorizationGroup("cost-center.authority")
    public final String COST_CENTER_VIEW = "cost-center.authority.view";
    @Getter
    @AuthorizationGroup("cost-center.authority")
    public final String COST_CENTER_INSERT = "cost-center.authority.insert";
    @Getter
    @AuthorizationGroup("cost-center.authority")
    public final String COST_CENTER_UPDATE = "cost-center.authority.update";
    @Getter
    @AuthorizationGroup("cost-center.authority")
    public final String COST_CENTER_DELETE = "cost-center.authority.delete";

    @Getter
    @AuthorizationGroup("movement-class.authority")
    public final String MOVEMENT_CLASS_VIEW = "movement-class.authority.view";
    @Getter
    @AuthorizationGroup("movement-class.authority")
    public final String MOVEMENT_CLASS_INSERT = "movement-class.authority.insert";
    @Getter
    @AuthorizationGroup("movement-class.authority")
    public final String MOVEMENT_CLASS_UPDATE = "movement-class.authority.update";
    @Getter
    @AuthorizationGroup("movement-class.authority")
    public final String MOVEMENT_CLASS_DELETE = "movement-class.authority.delete";

    @Getter
    @AuthorizationGroup("movement.authority")
    public final String MOVEMENT_VIEW = "movement.authority.view";
    @Getter
    @AuthorizationGroup("movement.authority")
    public final String MOVEMENT_INSERT = "movement.authority.insert";
    @Getter
    @AuthorizationGroup("movement.authority")
    public final String MOVEMENT_UPDATE = "movement.authority.update";
    @Getter
    @AuthorizationGroup("movement.authority")
    public final String MOVEMENT_PAY = "movement.authority.pay";
    @Getter
    @AuthorizationGroup("movement.authority")
    public final String MOVEMENT_DELETE = "movement.authority.delete";

    @Getter
    @AuthorizationGroup("card-invoice.authority")
    public final String CARD_INVOICE_PAY = "card-invoice.authority.pay";
    @Getter
    @AuthorizationGroup("card-invoice.authority")
    public final String CARD_INVOICE_VIEW = "card-invoice.authority.view";
    @Getter
    @AuthorizationGroup("card-invoice.authority")
    public final String CARD_INVOICE_PROCESS = "card-invoice.authority.process";
    @Getter
    @AuthorizationGroup("card-invoice.authority")
    public final String CARD_INVOICE_HISTORY = "card-invoice.authority.history";

    @Getter
    @AuthorizationGroup("balance-transfer.authority")
    public final String BALANCE_TRANSFER_VIEW = "balance-transfer.authority.view";
    @Getter
    @AuthorizationGroup("balance-transfer.authority")
    public final String BALANCE_TRANSFER_MAKE = "balance-transfer.authority.make";

    @Getter
    @AuthorizationGroup("financial-period.authority")
    public final String FINANCIAL_PERIOD_VIEW = "financial-period.authority.view";
    @Getter
    @AuthorizationGroup("financial-period.authority")
    public final String FINANCIAL_PERIOD_INSERT = "financial-period.authority.insert";
    @Getter
    @AuthorizationGroup("financial-period.authority")
    public final String FINANCIAL_PERIOD_DELETE = "financial-period.authority.delete";
    @Getter
    @AuthorizationGroup("financial-period.authority")
    public final String FINANCIAL_PERIOD_CLOSE = "financial-period.authority.close";
    @Getter
    @AuthorizationGroup("financial-period.authority")
    public final String FINANCIAL_PERIOD_DETAILS = "financial-period.authority.details";

    @Getter
    @AuthorizationGroup("closing.authority")
    public final String CLOSING_VIEW = "closing.authority.view";
    @Getter
    @AuthorizationGroup("closing.authority")
    public final String CLOSING_CLOSE = "closing.authority.close";
    @Getter
    @AuthorizationGroup("closing.authority")
    public final String CLOSING_PROCESS = "closing.authority.process";

    @Getter
    @AuthorizationGroup("account.authority")
    public final String ACCOUNTS_VIEW = "account.authority.view";
    @Getter
    @AuthorizationGroup("account.authority")
    public final String ACCOUNTS_INSERT = "account.authority.insert";
    @Getter
    @AuthorizationGroup("account.authority")
    public final String ACCOUNTS_UPDATE = "account.authority.update";
    @Getter
    @AuthorizationGroup("account.authority")
    public final String ACCOUNTS_DELETE = "account.authority.delete";

    @Getter
    @AuthorizationGroup("private-message.authority")
    public final String PRIVATE_MESSAGES_VIEW = "private-message.authority.view";
    @Getter
    @AuthorizationGroup("private-message.authority")
    public final String PRIVATE_MESSAGES_SEND = "private-message.authority.send";

    /**
     * Lista todas as authorities disponiveis para uso, este metodo e utilzado
     * para criar o admin no bootstrap da aplicacao
     *
     * @return um set com todas as authorities disponiveis
     */
    public Set<String> getAllAvailableAuthorities() {

        final Set<String> authorities = new HashSet<>();

        final Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            // verifica se a permissao tem grupo de permisao
            if (field.isAnnotationPresent(AuthorizationGroup.class)) {

                // adiciona as permissoes especificas
                try {
                    authorities.add((String) field.get(Authorization.this));
                } catch (IllegalAccessException ex) {
                }
            }
        }
        return authorities;
    }

    /**
     * Lista todas as authorities agrupadas pelo grupo de cada uma
     *
     * @return hashmap com os valores: grupo e itens do grupo
     */
    public HashMap<String, Set<String>> getAllAvailableAuthoritiesGrouped() {

        final HashMap<String, Set<String>> authorities = new HashMap<>();
        final Set<String> allAuthorities = this.getAllAvailableAuthorities();

        final Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(AuthorizationGroup.class)) {

                final String group = field.getAnnotation(AuthorizationGroup.class).value();

                if (!authorities.containsKey(group)) {

                    final Set<String> grouped = new HashSet<>();

                    allAuthorities.stream().filter((authority) -> 
                            (authority.contains(group))).forEach((authority) -> {
                        grouped.add(authority);
                    });
                    authorities.put(group, grouped);
                }
            }
        }
        return authorities;
    }
}
