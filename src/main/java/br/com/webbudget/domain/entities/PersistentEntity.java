/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The default implementation of a entity in the application.
 *
 * Every entity should extend this class to have the default behaviors of a JPA entity
 *
 * @author Arthur Gregorio
 *
 * @version 4.1.0
 * @since 1.0.0, 06/10/2013
 */
@ToString
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
public abstract class PersistentEntity implements IPersistentEntity<Long>, Serializable {

    @Id
    @Getter
    @GenericGenerator(
            name = "pooled_sequence_generator",
            strategy = "enhanced-sequence",
            parameters = {
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "5"),
                    @Parameter(name = "optimizer", value = "pooled-lo")
            })
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pooled_sequence_generator")
    private Long id;

    @Getter
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Getter
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    /**
     * Set the date of creation for this entity
     */
    @PrePersist
    protected void beforeInsert() {
        this.createdOn = LocalDateTime.now();
    }

    /**
     * Set the date of update for this entity
     */
    @PreUpdate
    protected void beforeUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isSaved() {
        return this.id != null && this.id != 0;
    }
}