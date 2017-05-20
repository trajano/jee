#!/bin/sh
[ -d  /config/resources/collective ] || collective join defaultServer --host=controller --port=9443 --keystorePassword=memberPassword --user=adminUser --password=adminPassword --autoAcceptCertificates
/opt/ibm/docker/docker-server run defaultServer