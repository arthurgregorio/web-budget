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
package br.com.webbudget.domain.model.entity.entries;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 03/04/2014
 */
@Entity
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
    @Enumerated
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_wallet")
    private Wallet wallet;

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
}
