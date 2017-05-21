#!/bin/bash
PASSWORD=$(openssl rand -base64 32)
[ -d  /config/resources/collective ] || collective create defaultServer --keystorePassword=$PASSWORD --createConfigFile=/config/collective-create-include.xml
/usr/sbin/sshd -D &

sed -i 's#<quickStartSecurity .*/>#<quickStartSecurity userName="adminUser" userPassword="adminPassword"/>#' /config/collective-create-include.xml

exec /opt/ibm/wlp/bin/server run defaultServer
