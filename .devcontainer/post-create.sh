#!/bin/bash
set -e
# Add Yarn GPG key and repo
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo gpg --dearmor -o /usr/share/keyrings/yarnkey.gpg
echo "deb [signed-by=/usr/share/keyrings/yarnkey.gpg] https://dl.yarnpkg.com/debian stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
# Update and install required packages
sudo apt update
sudo apt install -y libxext6 libxrender1 libxtst6 libxi6 libfreetype6

# Claude installation
curl -fsSL https://claude.ai/install.sh | bash

# Opencode installation
curl -fsSL https://opencode.ai/install | bash