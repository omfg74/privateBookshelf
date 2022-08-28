FROM ubuntu

RUN apt update && apt install openjdk-17-jdk -y
VOLUME ['/runtime']
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

RUN mkdir runtime
RUN mkdir /compile
COPY . /compile
RUN cd /compile &&  ./gradlew clean
RUN #cd /compile &&  ./gradlew rm -rf node_modules
RUN cd /compile &&  ./gradlew bootJar
RUN cp /compile/build/libs/book-shelf.jar /runtime
RUN chmod -R 777 /runtime

VOLUME ['/storage']

ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

EXPOSE 8080:8080

ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
    SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME \
    SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
#
#
ENTRYPOINT exec java $JVM_ARGS \
        -Dfile.encoding=utf-8 \
        -jar book-shelf.jar
