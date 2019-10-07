/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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

import br.com.webbudget.application.components.ui.AbstractBean;
import br.com.webbudget.application.components.ui.NavigationManager;
import br.com.webbudget.application.components.ui.table.LazyDataProvider;
import br.com.webbudget.application.components.ui.table.LazyModel;
import br.com.webbudget.application.components.ui.table.Page;
import br.com.webbudget.domain.entities.financial.CreditCardInvoice;
import br.com.webbudget.domain.entities.financial.InvoiceState;
import br.com.webbudget.domain.repositories.financial.CreditCardInvoiceRepository;
import br.com.webbudget.domain.services.CreditCardInvoiceService;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static br.com.webbudget.application.components.ui.NavigationManager.Parameter.of;

/**
 * Controller to the {@link CreditCardInvoice} view
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 09/03/2019
 */
@Named
@ViewScoped
public class CreditCardInvoiceBean extends AbstractBean implements LazyDataProvider<CreditCardInvoice> {

    @Getter
    @Setter
    private String filter;
    @Getter
    @Setter
    private InvoiceState invoiceState;

    @Getter
    private CreditCardInvoice invoice;

    @Getter
    private LazyDataModel<CreditCardInvoice> dataModel;

    @Inject
    private CreditCardInvoiceService creditCardInvoiceService;

    @Inject
    private CreditCardInvoiceRepository creditCardInvoiceRepository;

    /**
     * Constructor...
     */
    public CreditCardInvoiceBean() {
        this.dataModel = new LazyModel<>(this);
    }

    /**
     * Initialize view in listing mode
     */
    public void initialize() {
        this.invoiceState = InvoiceState.OPEN;
    }

    /**
     * Initialize UI to detail a {@link CreditCardInvoice}
     *
     * @param invoiceId to find the details
     */
    public void initializeDetail(long invoiceId) {
        this.invoice = this.creditCardInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice can't be found!"));
        this.invoice.orderPeriodMovements();
    }

    /**
     * {@inheritDoc}
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public Page<CreditCardInvoice> load(int first, int pageSize, String sortField, SortOrder sortOrder) {
        return this.creditCardInvoiceRepository.findAllBy(this.filter, this.invoiceState, first, pageSize);
    }

    /**
     * Call the method to close the {@link CreditCardInvoice}
     *
     * @param invoiceId to search for the invoice to close
     */
    public void doClose(long invoiceId) {
        this.creditCardInvoiceService.close(invoiceId);
        this.addInfo(true, "info.credit-card-invoice.closed");
    }

    /**
     * Clear the filters used to list the {@link CreditCardInvoice}
     */
    public void clearFilters() {
        this.filter = null;
        this.invoiceState = null;
    }

    /**
     * The possible states for the {@link CreditCardInvoice}
     *
     * @return a list of all {@link InvoiceState}
     */
    public InvoiceState[] getInvoiceStates() {
        return InvoiceState.values();
    }

    /**
     * Change to detail the {@link CreditCardInvoice}
     *
     * @param invoiceId to detail
     * @return the outcome to the detail page
     */
    public String changeToDetail(long invoiceId) {
        return NavigationManager.to("detailCreditCardInvoice.xhtml", of("id", invoiceId));
    }

    /**
     * Navigate back to the listing view
     *
     * @return the outcome to the listing view
     */
    public String changeToListing() {
        return NavigationManager.to("listCreditCardInvoices.xhtml");
    }

    /**
     * Navigate to the print page
     *
     * @return the outcome to the print page
     */
    public String changeToPrintInvoice() {
        Faces.setFlashAttribute("creditCardInvoice", this.invoice);
        return NavigationManager.to("cardInvoicePrint.xhtml");
    }
}