docker-compose -f docker-compose.run.yaml -p book-shelf down
docker-compose -f docker-compose.run.yaml -p book-shelf --env-file .env up -d