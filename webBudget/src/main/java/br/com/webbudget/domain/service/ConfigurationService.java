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
package br.com.webbudget.domain.service;

import br.com.webbudget.domain.entity.system.Configuration;
import br.com.webbudget.domain.repository.system.IConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 06/04/2014
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConfigurationService {

    @Autowired
    private IConfigurationRepository configurationRepository;

    /**
     * 
     * @param configuration
     * @return 
     */
    public Configuration saveConfiguration(Configuration configuration) {
        return this.configurationRepository.save(configuration);
    }
    
    /**
     * @return a configuracao default do sistema
     */
    @Transactional(readOnly = true)
    public Configuration loadDefault() {
        return this.configurationRepository.findDefault();
    }
}
