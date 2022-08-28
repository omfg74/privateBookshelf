FROM ubuntu

VOLUME [ "/source" ]
VOLUME [ "/runtime" ]
### openjdk:17 image has troubles running on arm v7 cpu
RUN apt update
RUN apt install openjdk-17-jdk -y


WORKDIR /source
RUN mkdir /scripts
#RUN #mkdir /executable
#WORKDIR /executable
RUN echo "#!/bin/bash" >> /scripts/compile.sh
RUN echo "echo \"start\"" >> /scripts/compile.sh
RUN echo "./gradlew bootJar" >> /scripts/compile.sh
RUN echo "mv build/libs/book-shelf.jar /runtime" >> /scripts/compile.sh
RUN echo "echo \"finish\"" >> /scripts/compile.sh
RUN chmod +x /scripts/compile.sh

RUN exit

ENTRYPOINT [ "/scripts/compile.sh" ]