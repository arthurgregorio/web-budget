/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.repositories.registration;

import br.com.webbudget.domain.entities.registration.Card;
import br.com.webbudget.domain.entities.registration.CardType;
import br.com.webbudget.domain.entities.registration.Card_;
import br.com.webbudget.domain.repositories.LazyDefaultRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The {@link Card} repository
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 04/03/2013
 */
@Repository
public interface CardRepository extends LazyDefaultRepository<Card> {

    /**
     * Find a {@link Card} by the number or type
     *
     * @param number the number of the card
     * @param type the type of the card defined by the {@link CardType} enum
     * @return an {@link Optional} of the card
     */
    Optional<Card> findByNumberAndCardType(String number, CardType type);

    /**
     * Find the {@link Card} with the {@link CardType} specified and status
     *
     * @param cardType the {@link CardType} to filter the {@link Card}
     * @param active which status you want to get
     * @return the {@link List} of {@link Card} found
     */
    List<Card> findByCardTypeAndActive(CardType cardType, boolean active);

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    default SingularAttribute<Card, Boolean> getEntityStateProperty() {
        return Card_.active;
    }

    /**
     * {@inheritDoc}
     *
     * @param filter
     * @return
     */
    @Override
    default Collection<Criteria<Card, Card>> getRestrictions(String filter) {
        return List.of(
                this.criteria().likeIgnoreCase(Card_.name, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Card_.number, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Card_.flag, this.likeAny(filter)),
                this.criteria().likeIgnoreCase(Card_.owner, this.likeAny(filter)));
    }
}
