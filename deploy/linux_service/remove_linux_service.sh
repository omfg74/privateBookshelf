#!/bin/bash

sudo systemctl stop bookshelf
sudo systemctl disable bookshelf
sudo rm -rf /opt/bookshelf/
sudo rm -rf /etc/systemd/system/bookshelf.service
sudo systemctl daemon-reload