# Private Book Shelf

# Simple file storage to keep my e-book in order

### 1. Develop database creation

#### 1.1 Docker

- Current developing database is configured to be run in docker. So it recommended to be installed.
- Or you may use another database. Just replace ```spring.datasource.url``` value in application.properties file to your
  db url.

#### 1.2 Create/Recreate

- run db-recreate.sh from project directory
- run src/main/resources/db/updateLocalDb.sh to create local

### 2. Deploy

#### 2.1 creating env variables

- simply create .env file in project dir and place there all variables you need. Example ```ENV_VAR=FOO```. Or copy them
  from env_example

#### 2.2 build and run

- run deploy.sh from command line.
- that`s all.

#### 2.3 Database

- Deployment database may conflict with developing one. Change database settings in docker-compose.run.yaml or
  docker-compose.yaml if you need to run them both.
- Do not forget to change application db settings if you decided to do it

### 3. Linux service

If you do not want to use docker you can run deploy as linux service

#### 3.1 Create linux service

Simply run ```sudo bash deploy/linux_service/install_service.sh```

Then configure /opt/bookshelf/app.config

Then run ```sudo systemctl start bookshelf```

profit!

#### 3.2 Remove linux service

run  ```sudo bash deploy/linux_service/remove_linux_service.sh```

#### 3.3 Rebuild app

run  ```sudo bash deploy/linux_service/create_jar.sh```
and new jar file will replace existing in /opt/bookshelf then run Then run ```sudo systemctl restart bookshelf``` to restart new jar file
