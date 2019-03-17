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
package br.com.webbudget.infrastructure.initializer;

import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Comparator;

/**
 * Default initializer for the application. This class will automatic discovery for all tasks to be performed at
 * initialization time
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/03/2019
 */
@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class ApplicationInitializer {

    @Inject
    private Logger logger;

    @Inject
    private ProjectStage projectStage;

    @Any
    @Inject
    private Instance<InitializationTask> tasks;

    /**
     * Call the tasks and log the execution status
     */
    @PostConstruct
    public void initialize() {
        this.logger.info("webBudget is now preparing the initialization tasks...");
        this.tasks.stream()
                .sorted(Comparator.comparingInt(InitializationTask::getPriority))
                .forEach(InitializationTask::run);
        this.logger.info("{} initialization tasks performed and the applications is now running in {} mode",
                this.tasks.stream().count(), this.projectStage);
    }
}
