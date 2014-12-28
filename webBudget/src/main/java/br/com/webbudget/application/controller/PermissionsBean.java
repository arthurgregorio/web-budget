package br.com.webbudget.application.controller;

import br.com.webbudget.application.components.permission.Authority;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import lombok.Getter;

/**
 * Bean utlizado pelo sistema para requisitar as authorities disponiveis no 
 * sistemas
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 29/06/2014
 */
@ManagedBean
@RequestScoped
public class PermissionsBean {

    @Getter
    private Authority authority;
    
    /**
     * Inicializa a authority
     */
    @PostConstruct
    protected void initialize() {
        this.authority = new Authority();
    }
}
