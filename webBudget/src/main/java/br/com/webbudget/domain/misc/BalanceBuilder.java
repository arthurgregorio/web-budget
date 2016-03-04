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
package br.com.webbudget.domain.misc;

import br.com.webbudget.domain.entity.wallet.Wallet;
import br.com.webbudget.domain.entity.wallet.WalletBalance;
import br.com.webbudget.domain.entity.wallet.WalletBalanceType;
import java.math.BigDecimal;

/**
 * Builder para saldos de carteiras, com ele fazemos o encapsulamento da logica 
 * de manipulacao de saldos atraves de uma interface fluente
 * 
 * Instanciaas desta classe tambem sao usadas para que os eventos de edicao
 * dos saldos de carteira acontecam sendo este o objeto trafegado entre os eventos
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 27/08/2015
 */
public final class BalanceBuilder {

    private final WalletBalance walletBalance;

    /**
     * Inicializa o builder criando uma instancia do saldo para que os metodos
     * possam ser chamados e assim tudo sendo preenchido
     */
    public BalanceBuilder() {
        this.walletBalance = new WalletBalance();
    }
    
    /**
     * @param wallet a carteira que estamos movimentando
     * @return o builder
     */
    public BalanceBuilder forWallet(Wallet wallet) {
        this.walletBalance.setTargetWallet(wallet);
        return this;
    }
    
    /**
     * @param wallet a carteira de origem em caso de tranferencia
     * @return o builder
     */
    public BalanceBuilder fromWallet(Wallet wallet) {
        this.walletBalance.setSourceWallet(wallet);
        return this;
    }
    
    /**
     * @param value o valor movimentado na carteira
     * @return o builder
     */
    public BalanceBuilder withMovementedValue(BigDecimal value) {
        this.walletBalance.setMovementedValue(value);
        return this;
    }
    
    /**
     * @param value o saldo anterior
     * @return o builder
     */
    public BalanceBuilder withOldBalance(BigDecimal value) {
        this.walletBalance.setOldBalance(value);
        return this;
    }
    
    /**
     * @param value o saldo atual (novo saldo)
     * @return o builder
     */
    public BalanceBuilder withActualBalance(BigDecimal value) {
        this.walletBalance.setActualBalance(value);
        return this;
    }
    
    /**
     * @param reason a razao pela qual estamos alterado o saldo 
     * @return o builder
     */
    public BalanceBuilder byTheReason(String reason) {
        this.walletBalance.setReason(reason);
        return this;
    }
    
    /**
     * @param walletBalanceType o tipo de movimentacao deste saldo
     * @return o builder
     */
    public BalanceBuilder andType(WalletBalanceType walletBalanceType) {
        this.walletBalance.setWalletBalanceType(walletBalanceType);
        return this;
    }
    
    /**
     * @param code o codigo do movimento a referenciar neste saldo
     * @return o builder
     */
    public BalanceBuilder referencingMovement(String code) {
        this.walletBalance.setMovementCode(code);
        return this;
    }
    
    /**
     * @return o saldo
     */
    public WalletBalance build() {
        return this.walletBalance;
    }
}
