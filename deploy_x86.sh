docker-compose -f docker-compose.x86.yaml -p book-shelf down
ex -s +"g/VERSION/d" -cwq .env
VERSION=$(git describe --abbrev=0 --tags)
echo ${VERSION}
echo APP_VERSION="${VERSION}" >>.env
docker-compose -f docker-compose.x86.yaml -p book-shelf --env-file .env up -d
