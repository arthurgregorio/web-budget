package br.com.webbudget.application;

/**
 * Enum usado para sabermos em qual estado a tela se encontra para que 
 * possamos manipular os componentes visuais a fim de reproduzir as ações para 
 * cada estado das ações no sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 07/03/2014
 */
public enum ViewState {

    LISTING, DELETE, EDIT, ADD, DETAILS, PAYMENT, CALCULATION;
}
