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

import br.com.webbudget.infraestructure.configuration.ApplicationUtils;
import javax.enterprise.context.RequestScoped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Servico de busca de enderecos para o cadastro de contatos do sistema
 * 
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.2.0, 16/04/2015
 */
@RequestScoped
public class AddressFinder {

    /**
     * Busca os dados referentes a um endereco partindo do CEP como referencia
     * 
     * @param zipcode o cep
     * @return o endereco
     */
    public Address findAddressByZipcode(String zipcode) {

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApplicationUtils.getConfiguration("ws.cep"))
                .build();
        
        final ZipcodeService zipcodeService 
                = restAdapter.create(ZipcodeService.class);
        
        return zipcodeService.findAddress(zipcode);
    }
    
    /**
     * Definição do servico de busca do CEP para o retrofit
     */
    public interface ZipcodeService {
        
        /**
         * @param zipcode
         * @return 
         */
        @GET("/ws/{zipcode}/json")
        Address findAddress(@Path("zipcode") String zipcode);
    }
    
    /**
     * A representacao concreta do endereco
     */
    @ToString
    @EqualsAndHashCode
    public static class Address {

        @Getter
        @Setter
        private String cep;
        @Getter
        @Setter
        private String logradouro;
        @Getter
        @Setter
        private String complemento;
        @Getter
        @Setter
        private String bairro;
        @Getter
        @Setter
        private String localidade;
        @Getter
        @Setter
        private String uf;
        @Getter
        @Setter
        private String ibge;
        
        /**
         * @return o nome completo do estado referente a unidade federativa
         */
        public String getFullUfName() {
            
            switch (this.uf) {
                case "AC": return "Acre";
                case "AL": return "Alagoas";
                case "AP": return "Amapá";
                case "AM": return "Amazonas";
                case "BA": return "Bahia";
                case "CE": return "Ceará";
                case "DF": return "Distrito Federal";
                case "ES": return "Espírito Santo";
                case "GO": return "Goiás";
                case "MA": return "Maranhão";
                case "MT": return "Mato Grosso";
                case "MS": return "Mato Grosso do Sul";
                case "MG": return "Minas Gerais";
                case "PA": return "Pará";
                case "PB": return "Paraíba";
                case "PR": return "Paraná";
                case "PE": return "Pernambuco";
                case "PI": return "Piauí";
                case "RJ": return "Rio de Janeiro";
                case "RN": return "Rio Grande do Norte";
                case "RS": return "Rio Grande do Sul";
                case "RO": return "Rondônia";
                case "RR": return "Roraima";
                case "SC": return "Santa Catarina";
                case "SP": return "São Paulo";
                case "SE": return "Sergipe";
                case "TO": return "Tocantins";
                default: return "Desconhecido";
            }
        }
    }
}
