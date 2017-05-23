#!/bin/bash
PASSWORD=$(openssl rand -base64 32)
#ssh-keygen -t rsa -f $HOME/.ssh/id_rsa -N ""
ssh-keygen -t rsa -N ""

# We can provision others this way later, but at least one node needs to be configured for JVM Elasticity.

#/opt/ibm/wlp/bin/collective registerHost $(hostname) \
#    --host=controller \
#    --port=9443 \
#    --user=adminUser \
#    --password=adminPassword \
#    --autoAcceptCertificates \
#    --sshPrivateKey=$HOME/.ssh/id_rsa \
#    --hostWritePath=/

while ! collective join defaultServer \
    --host=controller \
    --port=9443 \
    --user=adminUser \
    --password=adminPassword \
    --autoAcceptCertificates \
    --keystorePassword=$PASSWORD \
    --hostJavaHome=$JAVA_HOME \
    --createConfigFile=/config/collective-join-include.xml
do
  sleep 1 # wait for 1/10 of the second before check again
done

# Unregister host when the script will terminate
unregisterHost() {
    collective unregisterHost $(hostname) \
        --host=controller \
        --port=9443 \
        --user=adminUser \
        --password=adminPassword \
        --autoAcceptCertificates
    exit
}
trap unregisterHost 0

# start the defaultServer in the background needs to connect to properly register itself to the controller.
/opt/ibm/wlp/bin/server run defaultServer &

# Use sshd as the "daemon" process
/usr/sbin/sshd -D &
wait
