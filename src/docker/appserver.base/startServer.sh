#!/bin/sh
[ -d  /config/resources/collective ] || collective join defaultServer \
    --host=controller \
    --port=9443 \
    --keystorePassword=memberPassword \
    --user=adminUser \
    --password=adminPassword \
    --autoAcceptCertificates \
    --createConfigFile=/config/collective-join-include.xml

# [ -d  /config/resources/collective ] || /usr/bin/expect /config/join-collective.expect

exec /opt/ibm/wlp/bin/server run defaultServer
