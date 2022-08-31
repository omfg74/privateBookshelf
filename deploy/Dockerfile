FROM debian

RUN apt update
RUN apt install sudo -y
RUN apt install curl -y
RUN curl -sL https://deb.nodesource.com/setup_16.x | sudo bash -
RUN apt update
RUN apt -y install nodejs
RUN apt update && apt install openjdk-17-jdk -y
