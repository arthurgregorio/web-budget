### Development environment configurations

To help developers who are familiar with docker environments, use this docker-compose script to
build a local environment with an instance of Postgres and PgAdmin4, ready to use.

Simply run: 

```docker-compose up```

And you will get: 

- **Postgres 12** running at *localhost:5432*
- **PgAdmin4** running at *localhost:6060*

Database: _webbudget_ with username and password set to _sa_webbudget_ 

#### Restoring a previous dump

Just use the following command:

```cat your-dump.sql | docker exec -i wb_postgres psql -d webbudget -U sa_webbudget```

This command works on Windows (10+) and Linux systems

#### Importing PgAdmin4 configurations

Write your configurations at the *servers.json* file and them:

##### Copy it to the container: 

```docker cp servers.json wb_pgadmin:/tmp/servers.json```

##### Import it:

```docker exec -it wb_pgadmin python /pgadmin4/setup.py --load-servers /tmp/servers.json --user webbudget@localhost```

Or you can simply access the web interface and type your configurations on the config window.