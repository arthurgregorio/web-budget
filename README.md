webBudget
=========

Para detalhes sobre funcionálidades ou tecnologias utilizadas, consulte a página oficial do projeto em [webbudget.com.br](http://webbudget.com.br/)

#### Ambiente de demonstração:

O sistema possui um ambiente de demonstração aberto para que você possa conhecer a ferramenta, atualmente ele encontra-se disponibilizado no OpenShift através de uma conta free.

Como trata-se de um acesso que várias pessoas utilizam, pode haver algumas instabilidades devido a quantidade de acessos e por se tratar de um ambiente com recursos limitados apenas com propósito de demonstração.

Link para acesso: [https://teste-webbudget.rhcloud.com/](https://teste-webbudget.rhcloud.com/)

#### Executando com Docker:

Utilizando o compose, baixe [este arquivo](https://gist.github.com/arthurgregorio/2450728e0884173b1376b4f801f7dbc0) e salve em algum lugar do seu computador com o nome de _docker-compose.yml_, em seguida, na pasta onde salvou o arquivo execute o seguinte comando:

```
docker-compose up -d --no-recreate
```
Para mais informações sobre o webBudget + Docker, veja [este link](http://arthurgregorio.eti.br/blog/geral/webbudget-agora-com-docker/).

#### Outras informações:

- [Changelogs](https://github.com/arthurgregorio/web-budget/wiki#changelog)
- [Para desenvolvedores]() [*em desenvolvimento*]
