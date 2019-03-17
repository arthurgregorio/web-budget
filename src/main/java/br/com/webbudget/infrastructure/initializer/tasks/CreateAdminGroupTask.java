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

import br.com.webbudget.domain.entities.configuration.Authorization;
import br.com.webbudget.domain.entities.configuration.Grant;
import br.com.webbudget.domain.entities.configuration.Group;
import br.com.webbudget.domain.repositories.configuration.AuthorizationRepository;
import br.com.webbudget.domain.repositories.configuration.GrantRepository;
import br.com.webbudget.domain.repositories.configuration.GroupRepository;
import br.com.webbudget.infrastructure.initializer.InitializationTask;
import br.com.webbudget.infrastructure.initializer.TransactionalInitializationTask;
import org.apache.deltaspike.core.api.exclude.Exclude;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

import static org.apache.deltaspike.core.api.projectstage.ProjectStage.Production;
import static org.apache.deltaspike.core.api.projectstage.ProjectStage.SystemTest;

/**
 * {@link InitializationTask} to create the default administrators {@link Group}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/03/2019
 */
@Dependent
@Exclude(ifProjectStage = {Production.class, SystemTest.class})
public class CreateAdminGroupTask extends TransactionalInitializationTask {

    @Inject
    private GrantRepository grantRepository;
    @Inject
    private GroupRepository groupRepository;
    @Inject
    private AuthorizationRepository authorizationRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInsideTransaction() {

        final Group group = this.groupRepository
                .findByName("Administradores")
                .orElseGet(() -> new Group("Administradores"));

        if (!group.isSaved()) {
            this.groupRepository.save(group);
            final List<Authorization> authorizations = this.authorizationRepository.findAll();
            authorizations.forEach(authorization -> this.grantRepository.save(new Grant(group, authorization)));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int getPriority() {
        return 1;
    }
}
