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

import br.com.webbudget.domain.security.Partition;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.PartitionManager;

/**
 * Um producer para os recursos necessarios ao Picketlink:
 * 
 * {@link Partition} para que nossa producao do gerenciador de identidades ja 
 * saia configurada de fabrica
 * 
 * Recursos do PL sao identificados pela anotacao {@link PicketLink}
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/06/2015
 */
@ApplicationScoped
public class DefaultPatitionProducer {

    @Inject
    private PartitionManager partitionManager;
    
    /**
     * @return a particao default da aplicacao para que o sistema possa iniciar
     * o gerenciamento das identidades
     */
    @Produces
    @PicketLink
    Partition defaultPartitionProducer() {
        
        final Partition partition = this.partitionManager.getPartition(
                Partition.class, Partition.DEFAULT);

        if (partition == null) {
            throw new IllegalStateException("No default partition found");
        }

        return partition;
    }
}
