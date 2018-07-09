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

/**
 * A simple interface to define how to create a mail content provider
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/04/2018
 */
public interface MailContentProvider {

    /**
     * Return the content processed by the provider
     * 
     * @return the content in string for sending in the body of the message
     */
    String getContent();
}
