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
package br.com.webbudget.domain.entities.entries;

import br.com.webbudget.domain.entities.PersistentEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.metamodel.SingularAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
@Entity
@NoArgsConstructor
@Table(name = "cards")
@ToString(callSuper = true, of = {"number", "cardType"})
@EqualsAndHashCode(callSuper = true, of = {"number", "cardType"})
public class Card extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{card.name}")
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @NotEmpty(message = "{card.number}")
    @Column(name = "number", nullable = false, length = 45)
    private String number;
    @Getter
    @Setter
    @NotEmpty(message = "{card.flag}")
    @Column(name = "flag", nullable = false, length = 45)
    private String flag;
    @Getter
    @Setter
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Getter
    @Setter
    @Column(name = "expiration_day")
    private Integer expirationDay;
    @Getter
    @Setter
    @NotEmpty(message = "{card.owner}")
    @Column(name = "owner", nullable = false, length = 45)
    private String owner;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_wallet")
    private Wallet wallet;

    /**
     * 
     * @param name
     * @param number
     * @param flag
     * @param owner
     * @param blocked
     * @param cardType 
     */
    private Card(String name, String number, String flag, String owner, boolean blocked) {
        this.name = name;
        this.number = number;
        this.flag = flag;
        this.owner = owner;
        this.blocked = blocked;
    }

    /**
     * 
     * @param data
     * @param status
     * @return 
     */
    public static Card asExample(String data, boolean status) {
        if (StringUtils.isBlank(data)) {
            data = "";
        }
        return new Card(data, data, data, data, status);
    }

    /**
     *
     * @return
     */
    public static SingularAttribute<Card, ?>[] filterProperties() {
        return new SingularAttribute[]{Card_.name, Card_.number, Card_.flag, 
            Card_.owner, Card_.blocked};
    }


    /**
     * Um nome mais legivel para o cartao:
     *
     * @return nome + 4 ultimos digitos do cartao + bandeira
     */
    public String getReadableName() {

        final StringBuilder builder = new StringBuilder();

        builder.append(this.name);
        builder.append(" - ");

        // fix #31
        if (this.number.length() > 3) {
            builder.append(this.number.substring(this.number.length() - 4,
                    this.number.length()));
        } else {
            builder.append(this.number);
        }

        builder.append(" - ");
        builder.append(this.flag);

        return builder.toString();
    }

    /**
     * Retorna o numero do cartao escondendo alguns caracteres
     *
     * @return o numero do catao
     */
    public String getSecuredNumber() {

        final StringBuilder secured = new StringBuilder();

        if (this.number != null && this.number.length() >= 8) {

            secured.append(this.number.substring(0, 2));

            for (int i = 0; i < (this.number.length() - 2); i++) {
                secured.append("*");
            }
            secured.append(this.number.substring(
                    this.number.length() - 4, this.number.length()));
        } else {
            return this.number;
        }

        return secured.toString();
    }
    
    /**
     * @return se temos um cartao de credito
     */
    public boolean isCreditCard() {
        return this.cardType == CardType.CREDIT;
    }
    
    /**
     * @return se temos um cartao de debito
     */
    public boolean isDebitCard() {
        return this.cardType == CardType.DEBIT;
    }
}
