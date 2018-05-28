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
package br.com.webbudget.domain.entities.tools;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.com.webbudget.domain.entities.registration.CostCenter;
import br.com.webbudget.domain.entities.registration.MovementClass;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe responsavel pela representacao das configuracoes do sitema no banco
 * de dados
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 15/02/2015
 */
@Entity
@ToString(callSuper = true)
@Table(name = "configurations")
@EqualsAndHashCode(callSuper = true)
public class Configuration extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "show_wallet_balances", nullable = false)
    private boolean showWalletBalances;
    
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_invoice_default_cost_center")
    @NotNull(message = "{configuration.invoice-cost-center}")
    private CostCenter invoiceDefaultCostCenter;
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "id_invoice_default_movement_class")
    @NotNull(message = "{configuration.invoice-movement-class}")
    private MovementClass invoiceDefaultMovementClass;

    /**
     * Inicializa os atributos
     */
    public Configuration() {
        this.showWalletBalances = false;
    }
    
    /**
     * @return se esta configuracao eh valida para uso na geracao de faturas
     * dos cartoes de credito
     */
    public boolean isValidForCardInvoice() {
        return this.invoiceDefaultCostCenter != null 
                && this.invoiceDefaultMovementClass != null;
    }
}
