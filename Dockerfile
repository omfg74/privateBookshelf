FROM runtime

RUN mkdir /runtime
RUN chmod -R 777 /runtime
RUN mkdir /source
WORKDIR /source
COPY . /source
RUN ./gradlew clean
RUN ./gradlew vaadinClean
RUN ./gradlew vaadinBuildFrontend
RUN ./gradlew bootJar
RUN chmod -R 777 /runtime
COPY build/libs/book-shelf.jar /runtime
