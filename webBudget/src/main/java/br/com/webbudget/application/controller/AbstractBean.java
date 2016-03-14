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
package br.com.webbudget.application.controller;

import br.com.webbudget.domain.misc.chart.AbstractChartModel;
import br.com.webbudget.infraestructure.Translator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

/**
 * Bean utilizado como base para todos os outros beans da aplicacao. Nele esta
 * algumas funcionalidades base para que a pagina seja manipulada com mais faci-
 * lidade
 *
 * @author Arthur Gregorio
 *
 * @version 1.2.0
 * @since 1.0.0, 18/01/2015
 */
public abstract class AbstractBean implements Serializable {

    @Getter
    protected ViewState viewState;
    
    @Inject
    protected Logger logger;

    @Inject
    private Translator translator;
    
    @Inject
    private FacesContext facesContext;
    @Inject
    private RequestContext requestContext;
    
    /**
     * @return o nome do componente default de mensagens da view
     */
    public String getDefaultMessagesComponentId() {
        return "messages";
    }
    
    /**
     * Caso o nome do componente default de mensagens tenha sido setado, este
     * metodo invocado apos adicionar mensagens faz com que ele seja atualizado
     * automaticamente
     */
    private void updateDefaultMessages() {
        if (this.getDefaultMessagesComponentId() != null 
                && !this.getDefaultMessagesComponentId().isEmpty()) {
            this.temporizeHiding(this.getDefaultMessagesComponentId());
        }
    }

    /**
     * Traduz uma mensagem pelo bundle da aplicacao
     * 
     * @param message a chave da mensagem original
     * @return o texto
     */
    public String translate(String message) {
        return this.translator.translate(message);
    }
    
    /**
     * Atualiza um componente pelo seu id no contexto atual
     *
     * @param componentId o id do componente
     */
    protected void updateComponent(String componentId) {
        this.requestContext.update(componentId);
    }

    /**
     * Executa um JavaScript na pagina pelo FacesContext atual
     *
     * @param script o script a ser executado
     */
    protected void executeScript(String script) {
        this.requestContext.execute(script);
    }

    /**
     * Apenas abre uma dialog pelo seu widgetvar
     * 
     * @param widgetVar o widgetvar para abri-la
     */
    protected void openDialog(String widgetVar) {
        this.executeScript("PF('" + widgetVar + "').show()");
    }
    
    /**
     * Dado o id de um dialog, atualiza a mesma e depois abre pelo widgetvar
     * 
     * @param id o id da dialog para atualiza-la
     * @param widgetVar o widgetvar para abri-la
     */
    protected void updateAndOpenDialog(String id, String widgetVar) {
        this.updateComponent(id);
        this.executeScript("PF('" + widgetVar + "').show()");
    }

    /**
     * Fecha uma dialog aberta previamente
     *
     * @param widgetVar o widgetvar da dialog
     */
    protected void closeDialog(String widgetVar) {
        this.executeScript("PF('" + widgetVar + "').hide()");
    }

    /**
     * Dado um componente, atualiza o mesmo e depois temporiza o seu fechamento
     * 
     * @param componentId o id do componente
     */
    protected void temporizeHiding(String componentId) {
        this.updateComponent(componentId);
        this.executeScript("setTimeout(\"$(\'#" + componentId + "\').slideUp(300)\", 8000)");
    }

    /**
     * Redireciona o usuario para um determinada URL, caso haja um erro, loga 
     * 
     * @param url a url para o cara ser redirecionado
     */
    protected void redirectTo(String url) {
        try {
            this.facesContext.getExternalContext().redirect(url);
        } catch (Exception ex) {
            throw new RuntimeException(
                    String.format("Can't redirect to url [%s]", url));
        }
    }
    
    /**
     * Metodo para desempacotar a pilha de excessoes a fim de que possamos
     * tratar de forma mais elegante exceptions do tipo constraints violadas
     *
     * @param exception a exception que buscamos
     * @param stack a stack
     * @return se ela existe ou nao nao nesta stack
     */
    public boolean containsException(Class<? extends Exception> exception, Throwable stack) {

        // se nao tem stack nao ha o que fazer!
        if (stack == null) return false;
        
        // navegamos recursivamente na stack
        if (stack.getClass().isAssignableFrom(exception)) {
            return true;
        } else {
            return this.containsException(exception, stack.getCause());
        }
    }
    
    /**
     * Adiciona uma mensagem de informacao na tela
     * 
     * @param message a mensagem
     * @param parameters os parametros da mensagem
     * @param updateDefault se devemos ou nao atualizar o componente default
     */
    public void addInfo(boolean updateDefault, String message, Object... parameters) {
        Messages.addInfo(null, this.translate(message), parameters);
        if (updateDefault) this.updateDefaultMessages();
    }
    
    /**
     * Adiciona uma mensagem de erro na tela
     * 
     * @param message a mensagem
     * @param parameters os parametros da mensagem
     * @param updateDefault se devemos ou nao atualizar o componente default
     */
    public void addError(boolean updateDefault, String message, Object... parameters) {
        Messages.addError(null, this.translate(message), parameters);
        if (updateDefault) this.updateDefaultMessages();
    }
    
    /**
     * Adiciona uma mensagem de aviso na tela
     * 
     * @param message a mensagem
     * @param parameters os parametros da mensagem
     * @param updateDefault se devemos ou nao atualizar o componente default
     */
    public void addWarning(boolean updateDefault, String message, Object... parameters) {
        Messages.addWarn(null, this.translate(message), parameters);
        if (updateDefault) this.updateDefaultMessages();
    }
    
    /**
     * 
     * @param canvas
     * @param model 
     */
    public void drawDonutChart(String canvas, AbstractChartModel model) {
        this.executeScript("drawDonutChart(" + model.toJson() + ", '"+ canvas + "')");
    }
    
    /**
     * 
     * @param canvas
     * @param model 
     */
    public void drawLineChart(String canvas, AbstractChartModel model) {
        this.executeScript("drawLineChart(" + model.toJson() + ", '"+ canvas + "')");
    }
    
    /**
     * Executa uma fucking regra of Three para saber a porcentagem de um valor
     * sobre o outro
     * 
     * @param x o x da parada
     * @param total o total que seria o 100%
     * 
     * @return a porcentagem
     */
    protected int percentageOf(BigDecimal x, BigDecimal total) {
        
        // escala o X para nao haver erros de comparacao
        x = x.setScale(2, RoundingMode.HALF_UP);
        
        // se um dos dois valores for null retorna 0 de cara
        if (x == null || total == null) {
            return 0;
        }
        
        BigDecimal percentage;
        
        if (x.compareTo(total) >= 0) {
            return 100;
        } else {
            percentage = x.multiply(new BigDecimal(100))
                            .divide(total, 2, RoundingMode.HALF_UP);
        }
        
        return percentage.intValue() > 100 ? 100 : percentage.intValue();
    }

    /**
     * Enum para controle do estado de execucao da tela
     */
    protected enum ViewState {
        ADDING,
        LISTING,
        INSERTING,
        EDITING,
        DELETING,
        DETAILING;
    }
}
