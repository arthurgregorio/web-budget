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
package br.com.webbudget.application.components.ui.table;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents a page of data from the database
 *
 * @param <T> the type the page data
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 11/09/2015
 */
public class Page<T extends PersistentEntity> {

    @Getter
    public final List<T> content;
    @Getter
    public final int totalPages;

    /**
     * Create a new page
     * 
     * @param content the content
     * @param totalPages the total of possible pages
     */
    private Page(List<T> content, int totalPages) {
        this.content = checkNotNull(content);
        this.totalPages = totalPages;
    }
    
    /**
     * Create a page with empty content 
     * 
     * @return the empty page
     */
    public static <V extends PersistentEntity> Page<V> empty() {
        return new Page<>(Collections.emptyList(), 0);
    }
    
    /**
     * Create a new page of a given content and with the count of total pages
     * 
     * @param <V> the generic type of the page
     * @param content the content
     * @param totalPages the total count of pages
     * @return the page with the given content
     */
    public static <V extends PersistentEntity> Page<V> of(List<V> content, int totalPages) {
        return new Page<>(content, totalPages);
    }
}
