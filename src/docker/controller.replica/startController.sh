#!/bin/bash
PASSWORD=$(openssl rand -base64 32)
ssh-keygen -t rsa -f $HOME/.ssh/id_rsa -N ""
while ! collective replicate defaultServer \
    --host=controller \
    --port=9443 \
    --user=adminUser \
    --password=adminPassword \
    --keystorePassword=$PASSWORD \
    --createConfigFile=/config/collective-replicate-include.xml \
    --autoAcceptCertificates
do
    sleep 1
done

ROOTKEYS_PASSWORD=$(cat /controllerResources/collective/collective-create-include.xml | sed -n 's/.*<keyStore id="collectiveRootKeys" password="\([^"]*\)"/\1/p')
sed -i 's#\(<keyStore id="collectiveRootKeys" password="\)\(".*\)#\1'$ROOTKEYS_PASSWORD'\2#' /config/collective-replicate-include.xml 

# Spawn off the default server to add the replica
/opt/ibm/wlp/bin/server run defaultServer &

while ! ( echo QUIT | openssl s_client -connect $(hostname):9443 > /dev/null )
do
    sleep 1
done

while ! collective addReplica $(hostname):10010 \
    --user=adminUser \
    --password=adminPassword \
    --port=9443 \
    --host=controller \
    --autoAcceptCertificates
do
    sleep 1
done

# Unregister host when the script will terminate
unregisterHost() {
    collective removeReplica $(hostname):10010 \
        --host=controller \
        --port=9443 \
        --user=adminUser \
        --password=adminPassword \
        --autoAcceptCertificates
    exit
}
trap unregisterHost 0

# Use sshd as the "daemon" process
/usr/sbin/sshd -D &

wait
