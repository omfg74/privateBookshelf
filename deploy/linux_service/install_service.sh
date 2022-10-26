#!bin/bash

sudo mkdir /opt/bookshelf
chown -R $USER /opt/bookshelf
sudo touch /opt/bookshelf/bookshelf.config
sudo touch /opt/bookshelf/run.sh
sudo touch /etc/systemd/system/bookshelf.service

sudo echo "[Unit]" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "Description=Book Shelf" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "After=syslog.target network.target" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "[Service]" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "User=$USER" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "SuccessExitStatus=143" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "TimeoutStartSec=infinity" | tee -a /etc/systemd/system/bookshelf.service

sudo echo "Type=forking" | tee -a /etc/systemd/system/bookshelf.service

sudo echo "ExecStart=/opt/bookshelf/run.sh" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "ExecStop=/bin/kill -15 $MAINPID" | tee -a /etc/systemd/system/bookshelf.service

sudo echo "[Install]" | tee -a /etc/systemd/system/bookshelf.service
sudo echo "WantedBy=multi-user.target" | tee -a /etc/systemd/system/bookshelf.service


echo "-Dspring.datasource.url=jdbc:postgresql://localhost:5433/book_shelf" >>/opt/bookshelf/bookshelf.config
echo "-Dspring.datasource.username=admin" >>/opt/bookshelf/bookshelf.config
echo "-Dspring.datasource.password=admin" >>/opt/bookshelf/bookshelf.config
echo "-Dvaadin.productionMode=true" >>/opt/bookshelf/bookshelf.config
echo "-Dvaadin.servlet.heartbeatInterval=60" >>/opt/bookshelf/bookshelf.config
echo "-Dvaadin.servlet.closeIdleSessions=true" >>/opt/bookshelf/bookshelf.config
echo "-Dapp.version=""$(git describe --abbrev=0 --tags)" >>/opt/bookshelf/bookshelf.config


echo "#!/bin/bash" >>/opt/bookshelf/run.sh

echo "JAVA_HOME=/usr/bin/java" >>/opt/bookshelf/run.sh
echo "WORKDIR=/opt/bookshelf" >>/opt/bookshelf/run.sh
echo "JAVA_OPTIONS=\" -Xms256m -Xmx512m \$(cat /opt/bookshelf/bookshelf.config)\"" >>/opt/bookshelf/run.sh

echo "cd \$WORKDIR" >>/opt/bookshelf/run.sh
echo "\${JAVA_HOME} \$JAVA_OPTIONS -jar book-shelf.jar \$APP_OPTIONS" >>/opt/bookshelf/run.sh

sudo systemctl daemon-reload
sudo chmod -R 777 /opt/bookshelf/

bash ./gradlew clean
bash ./gradlew vaadinBuildFrontend
bash ./gradlew bootJar
cp build/libs/book-shelf.jar /opt/bookshelf
