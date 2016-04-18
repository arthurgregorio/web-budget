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
package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.tools.Configuration;
import br.com.webbudget.domain.model.service.ConfigurationService;
import br.com.webbudget.domain.model.service.MovementService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Controller para a pagina de configuracao do sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.3.0
 * @since 1.1.0, 23/03/2015
 */
@Named
@ViewScoped
public class ConfigurationBean extends AbstractBean {

    @Getter
    private Configuration configuration;
    
    @Getter
    private List<CostCenter> costCenters;
    @Getter
    private List<MovementClass> movementClasses;
    
    @Inject
    private MovementService movementService;
    @Inject
    private ConfigurationService configurationService;
    
    /**
     * Inicializa a configuracao default
     */
    public void initialize() {
        
        this.configuration = this.configurationService.loadDefault();

        this.costCenters = this.movementService.listCostCenters(false);
        
        // se nao houver uma configuracao, cria uma
        if (this.configuration == null) {
            this.configuration = new Configuration();
            this.movementClasses = this.movementService.listMovementClasses(false);
        } else {
            this.loadMovementClasses();
        }
    }
    
    /**
     * Salva a configuracao realizada
     */
    public void doSave() {
        
        try {
            this.configuration = this.configurationService
                    .saveConfiguration(this.configuration);

            this.addInfo(true, "configuration.saved", true);
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        } 
    }
    
    /**
     * Atualiza o combo de classes quando o usuário selecionar o centro de custo
     */
    public void loadMovementClasses() {
        this.movementClasses = this.movementService.listMovementClassesByCostCenterAndType(
                this.configuration.getInvoiceDefaultCostCenter(), null);
    }
}
