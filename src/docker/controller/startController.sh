#!/bin/sh
[ -d  /config/resources/collective ] || collective create defaultServer --keystorePassword=controllerKSPassword --createConfigFile=/config/collective-create-include.xml
exec /opt/ibm/wlp/bin/server run defaultServer
