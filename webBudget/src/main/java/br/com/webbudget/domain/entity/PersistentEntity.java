package br.com.webbudget.domain.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Classe base para indicar que se trata de uma entiade, nela temos os dados <br/>
 * basicos e comuns para que a classe possa ser persistente.
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/10/2013
 */
@MappedSuperclass
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
public abstract class PersistentEntity implements IPersistentEntity<Long>, Serializable {

    @Id
    @Column(name="id", unique=true, updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="inclusion", nullable=false)
    private Date inclusion;
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_edition")
    private Date lastEdition;

    /**
     * @return {@inheritDoc}
     */
    @Override
    public boolean isSaved(){
        return !(getId() == null || getId() == 0);
    }

    /**
     * Antes de inserir, define a hora de inserção e o usuário que inseriu
     */
    @PrePersist
    protected void prePersist() {
        this.inclusion = new Date();
    }
    
    /**
     * Antes de atualizar, muda a hora de atualização e o usuário que atualizou
     */
    @PreUpdate
    protected void preUpdate() {
        this.lastEdition = new Date();
    }
    
    /**
     * @return {@inheritDoc}
     */
    @Override
    public Long getId() {
        return this.id;
    }
}
