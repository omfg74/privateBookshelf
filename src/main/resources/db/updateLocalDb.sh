#!/bin/bash
java -jar \
	libs/liquibase.jar \
	--contexts=prod,test \
	--driver=org.postgresql.Driver \
	--defaultSchemaName=public \
	--classpath=libs/postgresql-42.2.8.jar \
	--changeLogFile=changelog/db.changelog-master.xml \
	--url="jdbc:postgresql://localhost:5433/book_shelf" \
	--username=admin \
	--password=admin \
	update