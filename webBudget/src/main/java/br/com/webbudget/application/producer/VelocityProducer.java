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
package br.com.webbudget.application.producer;

import java.util.Properties;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;

/**
 * Producer de instancias do motor de template do velocity
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 21/05/2015
 */
@ApplicationScoped
public class VelocityProducer {

    /**
     * @return produz uma instancia do velocity template para uso no sistema
     */
    @Produces
    VelocityEngine produceEngine() {

        final VelocityEngine engine = new VelocityEngine();

        // manda carregar os recursos dos resources
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        engine.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());

        final Properties properties = new Properties();

        // joga os logs do velocity no log do server
        properties.put("runtime.log.logsystem.class",
                "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        properties.put("runtime.log.logsystem.log4j.category", "velocity");
        properties.put("runtime.log.logsystem.log4j.logger", "velocity");

        engine.init(properties);

        return engine;
    }

    /**
     * @return produz um contexto para velocity engine
     */
    @Produces
    VelocityContext produceContext() {

        final VelocityContext context = new VelocityContext();

        // tools para trabalhar com data e numeros
        context.put("date", new DateTool());
        context.put("number", new NumberTool());

        return context;
    }
}
