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
package br.com.webbudget.domain.model.entity.contact;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Classe que mapeia os atributos necessarios para a existencia de um contato
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 07/04/2015
 */
@Entity
@Table(name = "contacts")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Contact extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotBlank(message = "{contact.validate.name}")
    @Column(name = "name", nullable = false, length = 90)
    private String name;
    @Setter
    @Getter
    @Column(name = "document", length = 25)
    private String document;
    @Setter
    @Getter
    @Past(message = "{contact.validate.birth-date}")
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;
    @Setter
    @Getter
    @Column(name = "other_informations", length = 255)
    private String otherInformations;

    @Setter
    @Getter
    @Column(name = "zipcode", length = 9)
    private String zipcode;
    @Setter
    @Getter
    @Column(name = "street", length = 90)
    private String street;
    @Setter
    @Getter
    @Column(name = "number", length = 8)
    private String number;
    @Setter
    @Getter
    @Column(name = "complement", length = 45)
    private String complement;
    @Setter
    @Getter
    @Column(name = "neighborhood", length = 45)
    private String neighborhood;
    @Setter
    @Getter
    @NotBlank(message = "{contact.validate.province}")
    @Column(name = "province", length = 45)
    private String province;
    @Setter
    @Getter
    @NotBlank(message = "{contact.validate.city}")
    @Column(name = "city", length = 45)
    private String city;
    @Setter
    @Getter
    @Column(name = "email", length = 90)
    private String email;
    
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    
    @Setter
    @Getter
    @NotNull(message = "{contact.validate.contact-type}")
    @Enumerated
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    /**
     * Eager pois ao pesquisarmos um contato os telefones devem vir junto
     * para compor a view adequadamente
     */
    @Getter
    @Setter
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "contact", fetch = EAGER, cascade = REMOVE)
    private List<Telephone> telephones;
    
    @Getter
    @Transient
    private List<Telephone> deletedTelephones;
    
    /**
     * Inicializa o contato
     */
    public Contact() {
        this.code = this.createContactCode();
        this.telephones = new ArrayList<>();
        this.deletedTelephones = new ArrayList<>();
    }
    
    /**
     * @return um codigo unico para este contato
     */
    private String createContactCode() {

        long decimalNumber = System.nanoTime();

        String generated = "";
        final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        synchronized (this.getClass()) {

            int mod;
            int authCodeLength = 0;

            while (decimalNumber != 0 && authCodeLength < 5) {

                mod = (int) (decimalNumber % 36);
                generated = digits.substring(mod, mod + 1) + generated;
                decimalNumber = decimalNumber / 36;
                authCodeLength++;
            }
        }
        return generated;
    }

    /**
     * Valida o documento do contato de acordo com seu tipo
     */
    public void validateDocument() {
        if (this.document != null && !this.document.isEmpty()) {
            this.contactType.validadeDocument(this.document.replace("\\w", ""));
        }
    }
    
    /**
     * @return o documento do contato formatado
     */
    public String getDocumentFormated() {
        return this.contactType.formatDocument(this.document);
    }
    
    /**
     * 
     * @param telephone 
     */
    public void addTelephone(Telephone telephone) {
        this.telephones.add(telephone);
    }

    /**
     *
     * @param telephone
     */
    public void removeTelephone(Telephone telephone) {
        this.telephones.remove(telephone);
        this.addDeletedTelephone(telephone);
    }

    /**
     * 
     * @param telephone 
     */
    private void addDeletedTelephone(Telephone telephone) {
        if (telephone.isSaved()) {
            this.deletedTelephones.add(telephone);
        }
    }
}
