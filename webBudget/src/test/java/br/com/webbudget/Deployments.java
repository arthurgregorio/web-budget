package br.com.webbudget;

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
public class Deployments {

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
        return ShrinkWrap.create(WebArchive.class, "webbudget-test.war")
                .addPackages(true, 
                        "br.com.turismoitaipu.cti.domain.model",
                        "br.com.webbudget.application.component.table")
                .addClasses(
                        LoggerProducer.class,
                        ApplicationUtils.class,
                        EntityManagerProducer.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource(new File("src/main/resources/webbudget.properties"))
                .addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(libraries);
    }
}
