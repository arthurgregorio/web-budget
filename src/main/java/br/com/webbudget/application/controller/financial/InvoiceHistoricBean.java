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
package br.com.webbudget.application.controller.financial;

import br.com.webbudget.application.controller.AbstractBean;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 13/03/2016
 */
@Named
@ViewScoped
public class InvoiceHistoricBean extends AbstractBean {

//    @Getter
//    @Setter
//    private Card card;
//    
//    @Getter
//    private List<Card> cards;
//
//    @Inject
//    private CardService cardService;
//    @Inject
//    private MovementService movementService;
//    
//    @Getter
//    private final AbstractLazyModel<CardInvoice> cardInvoicesModel;
//
//    /**
//     * Monta o lazy data model para a datatable na view
//     */
//    public InvoiceHistoricBean() {
//        
//        this.cardInvoicesModel = new AbstractLazyModel<CardInvoice>() {
//            @Override
//            public List<CardInvoice> load(int first, int pageSize, String sortField, 
//                    SortOrder sortOrder, Map<String, Object> filters) {
//                
//                final PageRequest pageRequest = new PageRequest();
//                
//                pageRequest
//                        .setFirstResult(first)
//                        .withPageSize(pageSize)
//                        .sortingBy(sortField, "inclusion")
//                        .withDirection(sortOrder.name());
//                
//                final Page<CardInvoice> page = 
//                        cardService.listInvoicesByCard(card, pageRequest);
//                
//                this.setRowCount(page.getTotalPagesInt());
//                
//                return page.getContent();
//            }
//        };
//    }
//    
//    /**
//     * Inicializa o que for preciso antes de renderizar a view
//     */
//    public void initialize() {
//        // listamos todos os cartoes
//        this.cards = this.cardService.listCards(null);
//    }
//    
//    /**
//     * Envia para a pagina de detalhes da fatura
//     * 
//     * @param cardInvoice a fatura a ser detalhada
//     * @return a pagina 
//     */
//    public String changeToDetail(CardInvoice cardInvoice) {
//        
//        // lista os movimentos da fatura
//        final List<Movement> movements =
//                this.movementService.listMovementsByCardInvoice(cardInvoice);
//        
//        // colocar os movimentos na fatura e seta na sessao
//        cardInvoice.setMovements(movements);
//        
//        Faces.setFlashAttribute("cardInvoice", cardInvoice);
//        
//        return "detailCardInvoice.xhtml?faces-redirect=true";
//    }
//    
//    /**
//     * Redireciona o usuario para ver o movimento referente a fatura
//     * 
//     * @param movementId o id do movimento
//     * @return a URL para visualizar o movimento
//     */
//    public String changeToViewMovement(long movementId) {
//        return "../movement/formMovement.xhtml?faces-redirect=true"
//                + "&movementId=" + movementId + "&detailing=true";
//    }
//    
//    /**
//     * @return a data atual em formato string
//     */
//    public String getActualDateAsString() {
//        return DateTimeFormatter.ofPattern("dd/MM/yyyy")
//                .format(LocalDate.now());
//    }
}