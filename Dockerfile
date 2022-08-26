FROM openjdk:17
VOLUME ['/runtime']
VOLUME ['/storage']
RUN microdnf install findutils
RUN mkdir runtime
COPY . /runtime
RUN cd /runtime &&  ./gradlew bootJar
RUN cp /runtime/build/libs/book-shelf.jar /runtime
WORKDIR /runtime

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
    SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME \
    SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD


ENTRYPOINT exec java $JVM_ARGS \
        -Dfile.encoding=utf-8 \
        -jar /runtime/book-shelf.jar
