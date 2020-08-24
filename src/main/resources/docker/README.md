### Importing PgAdmin4 configurations

Write your configurations at the *servers.json* file and them:

##### Copy it to the container: 

```docker cp servers.json wb_pgadmin:/tmp/servers.json```

##### Import it:

```docker exec -it wb_pgadmin python /pgadmin4/setup.py --load-servers /tmp/servers.json --user webbudget@localhost```

Or you can simply access the web interface and type your configurations on the config window.