package br.com.webbudget.infraestructure;

import br.com.webbudget.application.components.permission.Authority;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.entity.users.Permission;
import br.com.webbudget.domain.service.AccountService;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 18/02/2014
 */
@Component
public class Bootstrap {

    @Autowired
    private AccountService accountService;

    /**
     * Incializa o bootstrap da aplicacao
     */
    @PostConstruct
    void initialize() {
        
        // diz que a lingua padrao e pt_BR
        Locale.setDefault(new Locale("pt", "BR"));
        
        // checa os dados do BD
        this.checkForAdmin();
    }

    /**
     * Checa se o admin existe, se nao, cria um
     */
    private void checkForAdmin() {

        // busca pelo admin
        User user = this.accountService.findUserByUsername("admin");

        // checa se ele existe ou nao
        if (user == null) {

            user = new User();

            // em caso de nao, cria o user admin
            user.setName("Administrador");
            user.setEmail("contato@arthurgregorio.eti.br");
            user.setUsername("admin");
            user.setNewPassword("admin");

            // cria as permissoes
            final Set<Permission> permissions = new HashSet<>();
            
            // instanciamos a classe que contem todas as authorities
            final Authority authorities = new Authority();
            
            // lista todas as disponiveis
            for (String authority : authorities.getAllAvailableAuthorities()) {
                final Permission permission = new Permission();
                
                permission.setAuthority(authority);
                
                // seta na lista de permissoes
                permissions.add(permission);
            }
            
            // seta a permissao no usuario
            user.setPermissions(permissions);

            // salva o admin com todas as permissoes
            this.accountService.createAccount(user);
        }
    }
}
