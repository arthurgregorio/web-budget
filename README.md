webBudget
=========

#### Apresentação:

O webBudget é um sistema simples de controle financeiro para pequenas empresas ou uso pessoal. 

Através dele é possível realizar o controle de receitas e despesas por centro de custo e classes de movimentação, acompanhar por períodos as suas movimentações e visualizar através de gráficos como andam as finanças.

#### Principais Funcionalidades:

O sistema em sua versão atual possui várias funcionalidades baseadas na necessidade de um controle financeiro simples e eficaz. Sendo elas:

- Controle de entradas e saídas atavés de movimentos classificados por centro de custo e classe
- Controle de carteiras, contas bancárias ou carteira física
- Controle de cartões de crédito, com geração de faturas e acompanhamento de gastos
- Centros de custo e classes de despesas/receitas, com definição de margem de limite (orçamento previsto)
- Rateio de movimentos por centro de custo e classe de movimentação, ou seja, um movimento pode ser dividido em vários CC para melhorar divisão das contas
- Mecanismo interno de mensageria entre os usuários
- Períodos de lançamentos no estilo competência, com data de início e fim e acompanhamento gráfico dos movimentos 
- Controle de usuários através de permissões de acesso por grupos (visualização, inclusão, edição e deleção)
- Transferências entre carteiras e controle iterativo de saldos
- Movimentos fixos, com simulação de parcelamento ou apenas lançamento automático a cada período

Novas funcionálidades estão sempre em desenvolvimento, consulte a [lista de atividades do projeto](https://github.com/arthurgregorio/web-budget/issues) para propor uma ou acompanhar o que esta sendo feito!

#### Informações Técnicas:

O sistema foi desenvolvido utilizando uma plataforma baseada na tecnologia Java e seus frameworks. Possui um ambiente de execução simples de ser estruturado e de fácil manutenção. 

Dentre as tecnologias adotadas, as principais são:

- JSF 2.2 
- Primefaces 5
- JPA 2.1
- Hibernate 5
- Weld 2
- Picketlink 2.7
- Wildfly 9

Devido ao suporte a JPA, o banco de dados torna-se independente da aplicação, porém os SGBD's homologados para uso, 100% testados são *MySQL 5* ou *MariaDB 10*.

#### Uso e Comercialização:

De acordo com a licença GPLv3, não há restrições quanto ao uso deste software em âmbito comercial ou pessoal, porém, qualquer tipo de suporte/correção/implementação que se faça necessário e que esteja fora do ciclo normal de desenvolvimento da ferramenta, não esta incluído ou garantido pela licença citada. Para mais informações entre em contato pelo endereço contato@arthurgregorio.eti.br.

Ou ainda você poderá abrir uma requisição de implementação/correção/melhoria e aguardar até que esta seja aprovada e entre no cronograma de evolução da ferramenta. Você pode ainda contribuir para o projeto, realizando a implementação e disponibilizando para a comunidade de acordo com o estabelecido na licença, sem custos e de maneira livre.

#### Extras:

- [Changelog das versões](https://github.com/arthurgregorio/web-budget/wiki#changelog)
- [Ambiente de demonstração](https://github.com/arthurgregorio/web-budget/wiki#ambiente-de-demonstra%C3%A7%C3%A3o) 
- [Documentação de instalação e uso](https://github.com/arthurgregorio/web-budget/wiki) [*em desenvolvimento*]
- Vídeo de apresentação da ferramenta [*em desenvolvimento*]
