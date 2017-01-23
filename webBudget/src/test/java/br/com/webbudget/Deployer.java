/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget;

import br.com.webbudget.domain.model.entity.converter.JPALocalDateConverter;
import br.com.webbudget.domain.model.entity.converter.JPALocalTimeConverter;
import br.com.webbudget.application.producer.EntityManagerProducer;
import br.com.webbudget.application.producer.LoggerProducer;
import br.com.webbudget.infraestructure.configuration.ApplicationUtils;
import java.io.File;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * Classe helper para realizar o deploy do war de testes
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.1, 24/10/2016
 */
public class Deployer {

    /**
     * Executa o deploy da infra de testes
     * 
     * @return o archive para deploy
     */
    public static WebArchive deploy() {
        
        // libs do projeto
        final File[] libraries = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                .resolve()
                    .withTransitivity()
                    .asFile();
        
        // cria o arquivo war
        return ShrinkWrap.create(WebArchive.class, "wb-test.war")
                .addPackages(true, 
                        "br.com.webbudget.domain",
                        "br.com.webbudget.application.component.table")
                .addClasses(
                        JPALocalDateConverter.class,
                        JPALocalTimeConverter.class,
                        LoggerProducer.class,
                        ApplicationUtils.class,
                        EntityManagerProducer.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource(new File("src/main/resources/webbudget.properties"))
                .addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                .addAsLibraries(libraries);
    }
}
