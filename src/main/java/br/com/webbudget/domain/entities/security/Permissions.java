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

    // Mapeamento das permissoes do cadastro de usuarios
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

    // Mapeamento das permissoes do cadastro de grupos
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
    public @interface PermissionGrouper {

        /**
         * 
         * @return 
         */
        String value() default "";
    }
}
