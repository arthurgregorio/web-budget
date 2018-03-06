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
package br.com.webbudget.application.components.filter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import javax.persistence.metamodel.SingularAttribute;
import lombok.Getter;
import lombok.Setter;

/**
 * Filter mapping for use with deltaspike
 *
 * @param <T> the parametrized type of this class
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 19/02/2018
 */
public class Filter<T extends Filterable> {

    @Getter
    @Setter
    private String textFilter;
    @Getter
    @Setter
    private StatusFilterType statusFilter;

    private final T example;

    private final Set<SingularAttribute<T, ?>> filterAttributes;

    /**
     *
     */
    public Filter() {
        
        final Class<?> clazz = (Class<T>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        
        try {
             this.example = (T) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException("Can't create instance for filter class: "
                    + clazz.getName());
        }

        this.filterAttributes = ((Filterable) 
                this.example).getSingularAttributes();
    }

    /**
     *
     * @return
     */
    public T toExample() {
        return this.applyValueToAttributes();
    }

    /**
     * 
     * @return 
     */
    private T applyValueToAttributes() {
        
        this.filterAttributes.stream().forEach(attribute -> {
            
            final Field field = this.findField(attribute.getName());
            
            if (field.getName().equals("blocked")) {
                field.setBoolean(this.example, this.statusFilter.value());
            } else {
                field.setBoolean(this.example, this.textFilter);
            }
        });


        return example;
    }

    /**
     * 
     * @param name
     * @return 
     */
    private Field findField(String name) {
        for (Field field : this.example.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        throw new IllegalArgumentException(String.format(
                "Can't find field %s on class %s", name, 
                this.example.getName()));
    }
}
