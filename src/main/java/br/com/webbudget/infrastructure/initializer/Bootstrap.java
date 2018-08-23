/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * This class is the start point of the basic configurations for the application
 *
 * Through here whe configure the default user and all the data that need to be initialized in the database before
 * the first start
 *
 * This class is a EJB and runs on every start of the environment
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 27/12/2017
 */
@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class Bootstrap {

    @Inject
    private Logger logger;

    @Inject
    private EnvironmentInitializer initializer;

    /**
     * Initialize and do the job
     */
    @PostConstruct
    protected void initialize() {
        this.logger.info("Initializing application, this can take a minute or more...");
        this.initializer.initialize();
        this.logger.info("Initialization finished!");
   }
}
