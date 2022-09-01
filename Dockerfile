FROM runtime

RUN mkdir /runtime
RUN chmod -R 777 /runtime
COPY build/libs/book-shelf.jar /runtime
