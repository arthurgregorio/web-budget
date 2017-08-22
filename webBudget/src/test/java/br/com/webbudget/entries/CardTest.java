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
//package br.com.webbudget.entries;
//
//import br.com.webbudget.Deployer;
//import br.com.webbudget.domain.model.entity.entries.Card;
//import br.com.webbudget.domain.model.entity.entries.CardType;
//import br.com.webbudget.domain.model.service.CardService;
//import java.math.BigDecimal;
//import javax.inject.Inject;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.testng.Arquillian;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
///**
// * Teste unitario para validacao da parte de cartoes 
// * 
// * @author Arthur Gregorio
// *
// * @version 1.0.0
// * @since 2.3.1, 24/11/2016
// */
//public class CardTest extends Arquillian {
//
//    @Inject
//    private CardService cardService;
//    
//    /**
//     * @return o wararchive para deploy do teste
//     */
//    @Deployment
//    public static WebArchive deploy() {
//        return Deployer.deploy();
//    }
//    
//    /**
//     * Valida a insercao do objeto
//     */
//    @Test(priority = 1)
//    public void insertCard() {
//
//        final Card card = new Card();
//        
//        card.setCardType(CardType.CREDIT);
//        card.setCreditLimit(new BigDecimal(200));
//        card.setExpirationDay(10);
//        card.setFlag("VISA");
//        card.setName("Cartao Teste");
//        card.setNumber("1234567890");
//        card.setOwner("Jovem Brasileiro");
//        
//        this.cardService.saveCard(card);
//        
//        final Card saved = this.cardService
//                .findCardByNumberAndType("1234567890", CardType.CREDIT);
//        
//        Assert.assertNotNull(saved, "Test card not found");
//    }
//    
//    /**
//     * 
//     */
//    @Test(priority = 2)
//    public void listCards() {
//
//    }
//    
//    /**
//     * 
//     */
//    @Test(priority = 3)
//    public void updateCard() {
//
//    }
//    
//    /**
//     * 
//     */
//    @Test(priority = 4)
//    public void deleteCard() {
//
//    }
//}
