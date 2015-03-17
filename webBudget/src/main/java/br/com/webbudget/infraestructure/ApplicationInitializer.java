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
package br.com.webbudget.infraestructure;

import br.com.webbudget.application.permission.Authority;
import br.com.webbudget.domain.entity.users.User;
import br.com.webbudget.domain.entity.users.Permission;
import br.com.webbudget.domain.repository.user.IPermissionRepository;
import br.com.webbudget.domain.repository.user.IUserRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.1.0, 18/02/2014
 */
@Component
public class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private boolean bootstraped = false;

    @Autowired
    private PasswordEncoder passwordEncoder;
            
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPermissionRepository permissionRepository;

    /**
     *
     * @param event
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // checa os dados do BD e marca que jah foi inicializado
        if (!this.bootstraped) {
            this.checkForAdmin();
            this.bootstraped = true;
        }
    }

    /**
     * Checa se o admin existe, se nao, cria um
     */
    private void checkForAdmin() {

        // busca pelo admin
        User user = this.userRepository.findByUsername("admin");

        // checa se ele existe ou nao
        if (user == null) {

            user = new User();

            // em caso de nao, cria o user admin
            user.setName("Administrador");
            user.setEmail("contato@arthurgregorio.eti.br");
            user.setUsername("admin");
            user.setUnsecurePassword("admin");

            // cria as permissoes
            final Set<Permission> permissions = new HashSet<>();

            // instanciamos a classe que contem todas as authorities
            final Authority authorities = new Authority();

            // lista todas as disponiveis
            authorities.getAllAvailableAuthorities().stream().map((authority) -> {
                final Permission permission = new Permission();
                permission.setAuthority(authority);
                return permission;
            }).forEach((permission) -> {
                permissions.add(permission);
            });

            user.setPassword(this.passwordEncoder.encode(user.getUnsecurePassword()));

            // salva o usuario
            user = this.userRepository.save(user);

            // salvamos novamente as permissions
            for (Permission permission : permissions) {
                permission.setUser(user);
                this.permissionRepository.save(permission);
            }
        }
    }
}
