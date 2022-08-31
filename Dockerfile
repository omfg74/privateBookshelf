FROM runtime

WORKDIR /source
#ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
#    SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
#    SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
#    APP_BOOK_FILE_PATH=$APP_BOOK_FILE_PATH
#    APP_VERSION=$APP_VERSION
#    COM_SUN_MANAGEMENT_JMXREMOTE_PORT=$COM_SUN_MANAGEMENT_JMXREMOTE_PORT
#    COM_SUN_MANAGEMENT_JMXREMOTE_SSL=$COM_SUN_MANAGEMENT_JMXREMOTE_SSL
#    COM_SUN_MANAGEMENT_JMXREMOTE_AUTHENTICATE=$COM_SUN_MANAGEMENT_JMXREMOTE_AUTHENTICATE
#    JAVA_RMI_SERVER_HOSTNAME=$JAVA_RMI_SERVER_HOSTNAME
#    VAADIN_SERVLET_PRODUCTIONMODE=$VAADIN_SERVLET_PRODUCTIONMODE
#    VAADIN_SERVLET_HEARTBEATINTERVAL=$VAADIN_SERVLET_HEARTBEATINTERVAL
#    VAADIN_SERVLET_CLOSEIDLESESSIONS=$VAADIN_SERVLET_CLOSEIDLESESSIONS
#    VAADIN_PRODUCTIONMODE=$VAADIN_PRODUCTIONMODE

RUN mkdir /runtime
COPY . /source
RUN rm -rf node_modules
RUN rm -rf frontend
RUN chmod -R 777 /runtime
RUN ./gradlew clean
RUN ./gradlew vaadinClean
RUN ./gradlew vaadinBuildFrontend
RUN ./gradlew bootJar
RUN cp build/libs/book-shelf.jar /runtime
RUN chmod -R 777 /runtime
#RUN #java -jar  /tmp/runtime/book-shelf.jar