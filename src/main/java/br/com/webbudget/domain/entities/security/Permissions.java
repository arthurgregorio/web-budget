package br.com.webbudget.domain.entities.security;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.Getter;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 12/01/2018
 */
@Named
@ApplicationScoped
public class Permissions implements Serializable {

    @Getter
    @PermissionGrouper("user")
    private final String USER_INSERT = "user:insert";
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
    @PermissionGrouper("user")
    private final String USER_ACCESS = "user:access";

    @Getter
    @PermissionGrouper("group")
    private final String GROUP_INSERT = "group:insert";
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
    @PermissionGrouper("group")
    private final String GROUP_ACCESS = "group:access";
    
    @Getter
    @PermissionGrouper("user")
    private final String CONFIGURATION_ACCESS = "configuration:access";
    @Getter
    @PermissionGrouper("user")
    private final String CONFIGURATION_UPDATE = "configuration:update";

    @Getter
    @PermissionGrouper("card")
    private final String CARD_ACCESS = "card:access";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_STATISTICS = "card:statistics";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_INSERT = "card:insert";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_UPDATE = "card:update";
    @Getter
    @PermissionGrouper("card")
    private final String CARD_DELETE = "card:delete";

    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_ACCESS = "contact:access";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_INSERT = "contact:insert";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_UPDATE = "contact:update";
    @Getter
    @PermissionGrouper("contact")
    private final String CONTACT_DELETE = "contact:delete";

    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_ACCESS = "wallet:access";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_INSERT = "wallet:insert";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_UPDATE = "wallet:update";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_DELETE = "wallet:delete";
    @Getter
    @PermissionGrouper("wallet")
    private final String WALLET_ADJUST_BALANCE = "wallet:adjust-balance";

    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_ACCESS = "cost-center:access";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_INSERT = "cost-center:insert";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_UPDATE = "cost-center:update";
    @Getter
    @PermissionGrouper("cost-center")
    private final String COST_CENTER_DELETE = "cost-center:delete";

    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_ACCESS = "movement-class:access";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_INSERT = "movement-class:insert";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_UPDATE = "movement-class:update";
    @Getter
    @PermissionGrouper("movement-class")
    private final String MOVEMENT_CLASS_DELETE = "movement-class:delete";

    @Getter
    @PermissionGrouper("movement")
    private final String MOVEMENT_ACCESS = "movement:access";
    @Getter
    @PermissionGrouper("movement")
    private final String MOVEMENT_INSERT = "movement:insert";
    @Getter
    @PermissionGrouper("movement")
    private final String MOVEMENT_UPDATE = "movement:update";
    @Getter
    @PermissionGrouper("movement")
    private final String MOVEMENT_PAY = "movement:pay";
    @Getter
    @PermissionGrouper("movement")
    private final String MOVEMENT_DELETE = "movement:delete";
    
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_ACCESS = "fixed-movement:access";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_INSERT = "fixed-movement:insert";
    @Getter
    @PermissionGrouper("fixed-movement")
    private final String FIXED_MOVEMENT_UPDATE = "fixed-movement:update";
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
    @PermissionGrouper("card-invoice")
    private final String CARD_INVOICE_PAY = "card-invoice:pay";
    @Getter
    @PermissionGrouper("card-invoice")
    private final String CARD_INVOICE_ACCESS = "card-invoice:access";
    @Getter
    @PermissionGrouper("card-invoice")
    private final String CARD_INVOICE_PROCESS = "card-invoice:process";
    @Getter
    @PermissionGrouper("card-invoice")
    private final String CARD_INVOICE_HISTORIC = "card-invoice:historic";

    @Getter
    @PermissionGrouper("balance-transference")
    private final String BALANCE_TRANSFERENCE_ACCESS = "balance-transference:access";
    @Getter
    @PermissionGrouper("balance-transference")
    private final String BALANCE_TRANSFERENCE_MAKE = "balance-transference:make";

    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_ACCESS = "financial-period:access";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_INSERT = "financial-period:insert";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_DELETE = "financial-period:delete";
    @Getter
    @PermissionGrouper("financial-period")
    private final String FINANCIAL_PERIOD_DETAILS = "financial-period:details";

    @Getter
    @PermissionGrouper("closing")
    private final String CLOSING_ACCESS = "closing:access";
    @Getter
    @PermissionGrouper("closing")
    private final String CLOSING_CLOSE = "closing:close";
    @Getter
    @PermissionGrouper("closing")
    private final String CLOSING_PROCESS = "closing:process";

    @Getter
    @PermissionGrouper("message")
    private final String MESSAGE_SEND = "message:send";
    
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_ACCESS = "vehicle:access";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_INSERT = "vehicle:insert";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_UPDATE = "vehicle:update";
    @Getter
    @PermissionGrouper("vehicle")
    private final String VEHICLE_DELETE = "vehicle:delete";
    
    @Getter
    @PermissionGrouper("entries")
    private final String ENTRIES_ACCESS = "entries:access";
    @Getter
    @PermissionGrouper("entries")
    private final String ENTRIES_INSERT = "entries:insert";
    @Getter
    @PermissionGrouper("entries")
    private final String ENTRIES_UPDATE = "entries:update";
    @Getter
    @PermissionGrouper("entries")
    private final String ENTRIES_DELETE = "entries:delete";
    
    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_ACCESS = "refueling:access";
    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_INSERT = "refueling:insert";
    @Getter
    @PermissionGrouper("refueling")
    private final String REFUELING_DELETE = "refueling:delete";

    /**
     * @return a lista de permissoes em formato de autorizacao
     */
    public List<Authorization> toAuthorizationList() {

        final List<Authorization> authorizations = new ArrayList<>();

        // pega todos os campos da classe
        for (Field field : this.getClass().getDeclaredFields()) {

            // define acessivel
            field.setAccessible(true);

            try {
                // pega o valor do agrupador
                final PermissionGrouper grouper = field
                        .getAnnotation(PermissionGrouper.class);

                // pega o valor do campo
                final String permission
                        = String.valueOf(field.get(Permissions.this));

                final String functionality = grouper.value();

                // agrupa na lista 
                authorizations.add(
                        new Authorization(functionality,
                                permission.replace(functionality + ":", "")));
            } catch (IllegalAccessException ex) {
            }
        }
        return authorizations;
    }
    
    /**
     * 
     */
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface PermissionGrouper {

        /**
         * 
         * @return 
         */
        String value() default "";
    }
}
