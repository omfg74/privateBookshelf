FROM ubuntu

RUN apt update
RUN apt install openjdk-17-jdk -y

VOLUME [ "/runtime" ]

WORKDIR /runtime

EXPOSE 8080

CMD bash run.sh