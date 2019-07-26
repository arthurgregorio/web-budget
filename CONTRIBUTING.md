## configurando o projeto no wildfly 17.0.1

Faça Download do Wildfly e do driver jdbc do postgres

Exemple configuração

```

./bin/jboss-cli.sh

module add --name=org.postgres --resources=/tmp/postgresql-42.2.6.jar --dependencies=javax.api,javax.transaction.api
/subsystem=datasources/jdbc-driver=postgres:add(driver-name=postgres,driver-module-name=org.postgres,driver-class-name=org.postgresql.Driver)
data-source add --jndi-name=java:/datasources/webBudgetDS --name=webBudgetDS --connection-url=jdbc:postgresql://localhost/postgres --driver-name=postgres --user-name=postgres --password=postgres

```