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
package br.com.webbudget.infraestructure.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configura todo o contexto base da aplicacao junto ao Spring
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.1.0, 07/03/2015
 */
@Configuration
@ComponentScan({
    "br.com.webbudget.domain",
    "br.com.webbudget.infraestructure"
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("classpath:config/webbudget.properties")
public class ContextConfiguration {

    @Autowired
    private Environment env;

    /**
     * @return nosso property placeholder para que possamos trabalhar com os 
     * arquivos *.property
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * @return nosso message bundle gerenciado pelo spring
     */
    @Bean
    public ReloadableResourceBundleMessageSource configureMessageSource() {

        final ReloadableResourceBundleMessageSource source
                = new ReloadableResourceBundleMessageSource();

        source.setBasename("classpath:/i18n/messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(5);

        return source;
    }

    /**
     * Configura a sessao do javamail para que possamos enviar emails
     * 
     * @return a sessao do javamail a ser usada no {@link Postman}
     */
    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {

        final JavaMailSenderImpl transport = new JavaMailSenderImpl();

        transport.setHost(this.env.getProperty("mail.host"));
        transport.setPort(this.env.getProperty("mail.port", Integer.class));
        transport.setUsername(this.env.getProperty("mail.user"));
        transport.setPassword(this.env.getProperty("mail.password"));

        final Properties properties = new Properties();

        properties.put("mail.debug", this.env.getProperty("mail.debug"));
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);

        transport.setJavaMailProperties(properties);

        return transport;
    }

    /**
     * Nosso password encoder
     * 
     * OBS: ele esta aqui pois precisamos dele pronto antes de termos a confi-
     * guracao do contexto de seguranca devido ao fato de este precisar 
     * ser injetado na classe que configura a seguranca {@link SecurityConfiguration}
     * 
     * @return o nosso password encoder com forca setada param 13
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(13);
    }
}
