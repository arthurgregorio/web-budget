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
package br.com.webbudget.domain.entities.configuration;

import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the possible permissions of this system
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@Named
@ApplicationScoped
public class Permissions implements Serializable {

    @Getter
    @PermissionGrouper("card")
    private final String CARD_ACCESS = "card:access";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_ADD = "card:add";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_UPDATE = "card:update";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_DELETE = "card:delete";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_DETAIL = "card:detail";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_STATISTICS = "card:statistics";

    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_ACCESS = "contact:access";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_ADD = "contact:add";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_UPDATE = "contact:update";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_DELETE = "contact:delete";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_DETAIL = "contact:detail";

    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_ACCESS = "wallet:access";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_ADD = "wallet:add";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_UPDATE = "wallet:update";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_DELETE = "wallet:delete";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_DETAIL = "wallet:detail";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_ADJUST_BALANCE = "wallet:adjust-balance";

    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_ACCESS = "cost-center:access";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_ADD = "cost-center:add";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_UPDATE = "cost-center:update";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_DELETE = "cost-center:delete";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_DETAIL = "cost-center:detail";

    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_ACCESS = "movement-class:access";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_ADD = "movement-class:add";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_UPDATE = "movement-class:update";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_DELETE = "movement-class:delete";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_DETAIL = "movement-class:detail";

    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_ACCESS = "financial-period:access";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_REOPEN = "financial-period:reopen";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_ADD = "financial-period:add";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_DELETE = "financial-period:delete";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_DETAIL = "financial-period:detail";

    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_ACCESS = "vehicle:access";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_ADD = "vehicle:add";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_UPDATE = "vehicle:update";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_DELETE = "vehicle:delete";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_DETAIL = "vehicle:detail";

    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_ACCESS = "refueling:access";
    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_ADD = "refueling:add";
    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_DELETE = "refueling:delete";
    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_DETAIL = "refueling:detail";

    @Getter
    @PermissionGrouper("transference")
    private final String TRANSFERENCE_ACCESS = "transference:access";
    @Getter
    @PermissionGrouper("transference")
    private final String TRANSFERENCE_TRANSFER = "transference:transfer";

    @Getter
    @PermissionGrouper("period-movement")
    private final String PERIOD_MOVEMENT_ACCESS = "period-movement:access";
    @Getter
    @PermissionGrouper("period-movement")
    private final String PERIOD_MOVEMENT_ADD = "period-movement:add";
    @Getter
    @PermissionGrouper("period-movement")
    private final String PERIOD_MOVEMENT_UPDATE = "period-movement:update";
    @Getter
    @PermissionGrouper("period-movement")
    private final String PERIOD_MOVEMENT_DETAIL = "period-movement:detail";
    @Getter
    @PermissionGrouper("period-movement")
    private final String PERIOD_MOVEMENT_DELETE = "period-movement:delete";
    @Getter
    @PermissionGrouper("period-movement")
    private final String PERIOD_MOVEMENT_PAY = "period-movement:pay";

    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_ACCESS = "fixed-movement:access";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_ADD = "fixed-movement:add";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_UPDATE = "fixed-movement:update";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_DETAIL = "fixed-movement:detail";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_DELETE = "fixed-movement:delete";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_LAUNCH = "fixed-movement:launch";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_LAUNCHES = "fixed-movement:launches";

    @Getter
    @PermissionGrouper("credit-card-invoice")
    private final String CREDIT_CARD_INVOICE_PAY = "credit-card-invoice:pay";
    @Getter
    @PermissionGrouper("credit-card-invoice")
    private final String CREDIT_CARD_INVOICE_ACCESS = "credit-card-invoice:access";
    @Getter
    @PermissionGrouper("credit-card-invoice")
    private final String CREDIT_CARD_INVOICE_CLOSE = "credit-card-invoice:close";

    @Getter
    @PermissionGrouper("user")
    private final String USER_ACCESS = "user:access";
    @Getter
    @PermissionGrouper("user")
    private final String USER_ADD = "user:add";
    @Getter
    @PermissionGrouper("user")
    private final String USER_UPDATE = "user:update";
    @Getter
    @PermissionGrouper("user")
    private final String USER_DELETE = "user:delete";
    @Getter
    @PermissionGrouper("user")
    private final String USER_DETAIL = "user:detail";

    @Getter
    @PermissionGrouper("group")
    private final String GROUP_ACCESS = "group:access";
    @Getter
    @PermissionGrouper("group")
    private final String GROUP_ADD = "group:add";
    @Getter
    @PermissionGrouper("group")
    private final String GROUP_UPDATE = "group:update";
    @Getter
    @PermissionGrouper("group")
    private final String GROUP_DELETE = "group:delete";
    @Getter
    @PermissionGrouper("group")
    private final String GROUP_DETAIL = "group:detail";

    @Getter
    @PermissionGrouper("configuration")
    private final String CONFIGURATION_ACCESS = "configuration:access";

    @Getter
    @PermissionGrouper("closing")
    private final String CLOSING_ACCESS = "closing:access";

    /**
     * Method to process this class with reflection and build a list of {@link Authorization} to be used in the
     * {@link Group} creation form
     *
     * @return the {@link List} of the possible {@link Authorization}
     */
    public List<Authorization> toAuthorizationList() {

        final List<Authorization> authorizations = new ArrayList<>();

        // get all the fields of the class
        for (Field field : this.getClass().getDeclaredFields()) {

            field.setAccessible(true);

            try {
                final PermissionGrouper grouper = field.getAnnotation(PermissionGrouper.class);

                final String permission = String.valueOf(field.get(Permissions.this));

                final String functionality = grouper.value();

                authorizations.add(new Authorization(functionality, permission.replace(functionality + ":", "")));
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Can't parse the authorizations with reflection! Contact the administrator.");
            }
        }
        return authorizations;
    }

    /**
     * The grouper annotation to mark all the fields of this class by which functionality belongs to
     */
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface PermissionGrouper {

        /**
         * The value of this annotation is the functionality
         *
         * @return the functionality
         */
        String value() default "";
    }
}
