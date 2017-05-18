#!/bin/sh
[ -d  /config/resources/collective ] || /usr/bin/expect join-collective.expect
/opt/ibm/docker/docker-server run defaultServer