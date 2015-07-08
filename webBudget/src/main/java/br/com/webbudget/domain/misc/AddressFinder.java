package br.com.webbudget.domain.misc;

import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Servico de busca de enderecos para o cadastro de contatos do sistema
 * 
 * @author Arthur Gregorio
 *
 * @version 1.1.0
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
        
//        final String wsURL = this.configBundle.getString("ws.cep");
//        
//        // se a url estiver zicada ja tora o pau e nao deixa passar
//        if(wsURL == null || wsURL.isEmpty()) {
//            throw new IllegalStateException("Invalid URL for address search");
//        }
//
//        final RestTemplate template = new RestTemplate();
//        
//        return template.getForObject(String.format(wsURL, zipcode), Address.class);
        return null;
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
