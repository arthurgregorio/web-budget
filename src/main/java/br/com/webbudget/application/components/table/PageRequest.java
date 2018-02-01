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
package br.com.webbudget.application.components.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortMeta;

/**
 * Builder para producao dos filtros da pesquisa lazy
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.1.0, 05/09/2015
 */
public class PageRequest {

    @Getter
    private int firstResult;
    @Getter
    private int pageSize;
    @Getter
    private SortDirection sortDirection;
    @Getter
    private boolean multiSort;

    private List<MultiSortField> multiSortFields;

    private String sortField;
    private String defaultField;

    /**
     * Inicializa o que for necessario
     */
    public PageRequest() {
        this.sortDirection = SortDirection.DESC;
    }

    /**
     * @param first o primeiro resultado
     * @return o builder
     */
    public PageRequest setFirstResult(int first) {
        this.firstResult = first;
        return this;
    }

    /**
     * @param size o tamanho maximo da pagina
     * @return o builder
     */
    public PageRequest withPageSize(int size) {
        this.pageSize = size;
        return this;
    }

    /**
     * @param field o campo a ser usado como ordenador
     * @param defaultField o campo a ser usado como ordenador padrao
     * @return o builder
     */
    public PageRequest sortingBy(String field, String defaultField) {

        this.multiSort = false;

        this.multiSortFields = null;

        this.sortField = field;
        this.defaultField = defaultField;
        return this;
    }

    /**
     * Metodo utilizado quando o multisort da datatable esta habilitado
     *
     * @param sortMetas os campos para sort
     * @param defaultField o campo default
     * @return
     */
    public PageRequest multiSortingBy(List<SortMeta> sortMetas, String defaultField) {

        this.multiSort = true;

        this.multiSortFields = this.sortMetaToSortFields(sortMetas);
        this.defaultField = defaultField;
        return this;
    }

    /**
     * @param direction a direcao de ordenacao da tabela
     * @return o builder
     */
    public PageRequest withDirection(String direction) {

        switch (direction) {
            case "ASCENDING":
                this.sortDirection = SortDirection.ASC;
                break;
            case "DESCENDING":
                this.sortDirection = SortDirection.DESC;
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid sort direction: " + direction);
        }

        return this;
    }

    /**
     * @return por qual campos estamos fazendo sort na tabela
     */
    public String getSortField() {
        return StringUtils.isBlank(this.sortField) ? this.defaultField : this.sortField;
    }

    /**
     * @return a lista de campos para sort da pesquisa
     */
    public List<MultiSortField> getMultiSortFields() {
        this.multiSortFields.add(new MultiSortField("inclusion", "DESCENDING"));
        return Collections.unmodifiableList(this.multiSortFields);
    }

    /**
     * Converte as metas de sort do primefaces para uma lista legivel pela
     * camada de dominio do sistema
     *
     * @param metas as metas vindas das view
     * @return a lista de campos para sort
     */
    private List<MultiSortField> sortMetaToSortFields(List<SortMeta> metas) {

        final List<MultiSortField> fields = new ArrayList<>();

        if (metas != null) {
            metas.forEach(meta -> {
                fields.add(new MultiSortField(meta.getSortField(),
                        meta.getSortOrder().name()));
            });
        }

        return fields;
    }

    /**
     * A direcao de sort do filtro
     */
    public enum SortDirection {
        ASC, DESC;
    }

    /**
     * Um encapsulamento da logica de multisorting do primefaces para hibernate
     */
    public class MultiSortField {

        @Getter
        private final String sortField;
        @Getter
        private final SortDirection direction;

        /**
         *
         * @param sortField
         * @param direction
         */
        public MultiSortField(String sortField, String direction) {

            this.sortField = sortField;

            switch (direction) {
                case "ASCENDING":
                    this.direction = SortDirection.ASC;
                    break;
                case "DESCENDING":
                    this.direction = SortDirection.DESC;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Invalid sort direction: " + direction);
            }
        }
    }
}
