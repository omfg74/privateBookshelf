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

- run deploy_x86.sh from command line.
- that`s all.
- or if you got any troubles with container build you can run deploy_arm.sh it works on any platform.

#### 2.3 Database

- Deployment database may conflict with developing one. Change database settings in docker-compose.run.yaml or docker-compose.yaml if you need to run them both.
- Do not forget to change application db settings if you decided to do it  
