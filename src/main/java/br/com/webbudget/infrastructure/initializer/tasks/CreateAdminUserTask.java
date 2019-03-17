/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infrastructure.initializer.tasks;

import br.com.webbudget.domain.entities.configuration.Group;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.repositories.configuration.GroupRepository;
import br.com.webbudget.domain.repositories.configuration.UserRepository;
import br.com.webbudget.infrastructure.initializer.InitializationTask;
import br.com.webbudget.infrastructure.initializer.TransactionalInitializationTask;
import br.eti.arthurgregorio.shiroee.auth.PasswordEncoder;
import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.api.projectstage.ProjectStage.Production;
import org.apache.deltaspike.core.api.projectstage.ProjectStage.SystemTest;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * {@link InitializationTask} used to create the default admin {@link User}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 17/03/2019
 */
@Dependent
@Exclude(ifProjectStage = {Production.class, SystemTest.class})
public class CreateAdminUserTask extends TransactionalInitializationTask {

    @Inject
    private UserRepository userRepository;
    @Inject
    private GroupRepository groupRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInsideTransaction() {
        this.userRepository.findByUsername("admin").ifPresentOrElse(user -> {/* do nothing */}, () -> {

            final Group group = this.groupRepository
                    .findByName("Administradores")
                    .orElseThrow(() -> new IllegalStateException("Can't find the Administrators group"));

            final User user = new User();

            user.setName("Administrador");
            user.setEmail("contato@webbudget.com.br");
            user.setUsername("admin");
            user.setPassword(this.passwordEncoder.encryptPassword("admin"));
            user.setGroup(group);

            this.userRepository.save(user);
        });
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int getPriority() {
        return 2;
    }
}
