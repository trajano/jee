#!/bin/bash
createCollective() {
    PASSWORD=$(openssl rand -base64 32)
    collective create defaultServer \
        --keystorePassword=$PASSWORD \
        --createConfigFile=/config/resources/collective/collective-create-include.xml
    sed -i 's#<variable .*/>#<variable name="defaultHostName" value="controller" />#' /config/resources/collective/collective-create-include.xml
    sed -i 's#<quickStartSecurity .*/>#<quickStartSecurity userName="adminUser" userPassword="adminPassword"/>#' /config/resources/collective/collective-create-include.xml
}
[ -e  /config/resources/collective/collective-create-include.xml ] || createCollective
/usr/sbin/sshd -D &

exec /opt/ibm/wlp/bin/server run defaultServer
