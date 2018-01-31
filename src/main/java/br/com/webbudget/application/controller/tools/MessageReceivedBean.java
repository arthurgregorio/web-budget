/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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

import br.com.webbudget.infraestructure.components.table.AbstractLazyModel;
import br.com.webbudget.infraestructure.components.table.Page;
import br.com.webbudget.infraestructure.components.table.PageRequest;
import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.exceptions.ApplicationException;
import br.com.webbudget.domain.entities.tools.UserMessage;
import br.com.webbudget.domain.services.MessagingService;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 06/04/2016
 */
@Named
@ViewScoped
public class MessageReceivedBean extends AbstractBean {

//    @Getter
//    @Setter
//    private String filter;
//    
//    @Getter
//    @Setter
//    private UserMessage userMessage;
//    
//    @Inject
//    private MessagingService messagingService;
//    
//    @Getter
//    private final AbstractLazyModel<UserMessage> messagesModel;
//    
//    /**
//     *
//     */
//    public MessageReceivedBean() {
//
//        this.messagesModel = new AbstractLazyModel<UserMessage>() {
//            @Override
//            public List<UserMessage> load(int first, int pageSize, String sortField,
//                    SortOrder sortOrder, Map<String, Object> filters) {
//                
//                // constroi o filtro
//                final PageRequest pageRequest = new PageRequest();
//
//                pageRequest
//                        .setFirstResult(first)
//                        .withPageSize(pageSize)
//                        .sortingBy(sortField, "inclusion")
//                        .withDirection(sortOrder.name());
//
//                final Page<UserMessage> page = messagingService
//                        .listReceivedMessages(filter, pageRequest);
//                
//                this.setRowCount(page.getTotalPagesInt());
//                
//                return page.getContent();
//            }
//        };
//    }
//    
//    /**
//     * 
//     */
//    public void initializeList() {
//        this.viewState = ViewState.LISTING;
//    }
//    
//    /**
//     * 
//     * @param userMessageId
//     * @param viewState 
//     */
//    public void initializeDetailing(long userMessageId, String viewState) {
//        
//        this.viewState = ViewState.valueOf(viewState);
//        
//        this.userMessage = this.messagingService
//                .detailReceivedMessage(userMessageId);
//    }
//    
//    /**
//     * 
//     * @return 
//     */
//    public String doDelete() {
//        try {
//            this.messagingService.deleteUserMessage(this.userMessage);
//            return this.changeTolist();
//        } catch (InternalServiceError ex) {
//            this.addError(true, ex.getMessage(), ex.getParameters());
//            return null;
//        } catch (Exception ex) {
//            this.logger.error(ex.getMessage(), ex);
//            this.addError(true, "error.undefined-error", ex.getMessage());
//            return null;
//        }
//    }
//    
//    /**
//     * Redireciona para pagina de detalhes do vendedor
//     */
//    public void changeToDetail() {
//        this.redirectTo("detailReceivedMessage.xhtml?faces-redirect=true&id=" 
//                + this.userMessage.getId() + "&viewState=" + ViewState.DETAILING);
//    }
//    
//    /**
//     * @param userMessageId
//     * @return 
//     */
//    public String changeToDelete(long userMessageId) {
//        return "detailReceivedMessage.xhtml?faces-redirect=true&id=" + userMessageId 
//                + "&viewState=" + ViewState.DELETING;
//    }
//    
//    /**
//     * @return volta para a listagem
//     */
//    public String changeTolist() {
//        return "listReceivedMessages.xhtml?faces-redirect=true";
//    }
}
