docker-compose -f docker-compose.run.yaml -p book-shelf down
ex -s +"g/VERSION/d" -cwq .env
VERSION=$(git describe --abbrev=0 --tags)
echo ${VERSION}
ECHO APP_VERSION="${VERSION}" >>.env
docker-compose -f docker-compose.run.yaml -p book-shelf --env-file .env up -d
