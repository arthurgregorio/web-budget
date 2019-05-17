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
package br.com.webbudget.infrastructure.mail;

import br.com.webbudget.domain.exceptions.BusinessLogicException;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple content provider that use Mustache as template processor
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 3.0.0, 03/04/2018
 */
public class MustacheProvider implements MailContentProvider {

    private final Mustache mustache;

    private final Map<Object, Object> data;

    /**
     * Constructor
     *
     * @param template the template file name inside src/java/resources/mail
     */
    public MustacheProvider(String template) {

        this.data = new HashMap<>();

        final MustacheFactory factory = new DefaultMustacheFactory();
        this.mustache = factory.compile("/mail/" + template);
    }

    /**
     * Add some content to the provider
     *
     * @param key the key to use on the template
     * @param value the value to retrieve through the key in the template
     */
    public void addContent(Object key, Object value) {
        this.data.put(key, value);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String getContent() {

        final StringWriter writer = new StringWriter();

        try {
            this.mustache.execute(writer, this.data).flush();
        } catch (IOException ex) {
            throw new BusinessLogicException("error.core.email-content-error", ex);
        }

        return writer.toString();
    }
}