package br.com.webbudget.domain.entity;

import java.io.Serializable;

/**
 * Interface que devines os metodos minimos que uma entidade deve possuir para <br/>
 * ser considerada uma entitdade valida na regra de negocios deste sistema
 * 
 * @param <T> qualquer coisa que seja serializavel
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/10/2013
 */
public interface IPersistentEntity <T extends Serializable> {

    /**
     * Getter para o ID da entidade
     * 
     * @return o id da entidade
     */
    public Long getId();

    /**
     * Metodo que indica se uma entidade ja foi ou nao persistida (salva)
     * 
     * @return se a entidade ja foi persistida, retorna <code>true</code> indicando <br/>
     * que a mesma ja foi salva se nao retorna <code>false</code>
     */
    public boolean isSaved();
}
