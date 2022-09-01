if [[ "$(docker images -q runtime 2>/dev/null)" == "" ]]; then
  docker build -t runtime deploy/.
fi
docker-compose -f docker-compose.armv7.yaml -p book-shelf down

docker image rm $(docker images -q book-shelf_java-runtime) --force
docker image rm $(docker images -q book-shelf_db_init) --force

docker rm -q runtime -y
#./gradlew clean
#./gradlew vaadinClean
#./gradlew vaadinBuildFrontend
#./gradlew bootJar
docker-compose -f docker-compose.armv7.yaml -p book-shelf down
ex -s +"g/VERSION/d" -cwq .env
VERSION=$(git describe --abbrev=0 --tags)
echo ${VERSION}
echo APP_VERSION="${VERSION}" >>.env
docker-compose -f docker-compose.armv7.yaml build --no-cache && docker-compose -f docker-compose.armv7.yaml -p book-shelf --env-file .env up -d
