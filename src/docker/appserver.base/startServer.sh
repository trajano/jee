#!/bin/bash
# wait for controller to be up
while ! curl --insecure -s https://controller:9443/ --output /dev/null; do
  sleep 0.1 # wait for 1/10 of the second before check again
done

# Joining the collective needs to be done at runtime as the controller needs to be up.

PASSWORD=$(openssl rand -base64 32)
ssh-keygen -t rsa -f $HOME/.ssh/id_rsa -N ""

/opt/ibm/wlp/bin/collective join defaultServer \
    --host=controller \
    --port=9443 \
    --user=adminUser \
    --password=adminPassword \
    --autoAcceptCertificates \
    --keystorePassword=$PASSWORD \
    --sshPrivateKey=$HOME/.ssh/id_rsa \
    --hostJavaHome=$JAVA_HOME \
    --createConfigFile=/config/collective-join-include.xml

# Unregister host when the script will terminate
unregisterHost() {
    /opt/ibm/wlp/bin/collective unregisterHost $(hostname) \
        --host=controller \
        --port=9443 \
        --user=adminUser \
        --password=adminPassword \
        --autoAcceptCertificates
    exit
}
trap unregisterHost 0

#[ -d  /config/resources/collective ] || /opt/ibm/wlp/bin/collective registerHost $(hostname) \
#    --host=controller \
#    --port=9443 \
#    --user=adminUser \
#    --password=adminPassword \
#    --autoAcceptCertificates \
#    --sshPrivateKey=$HOME/.ssh/id_rsa
#    --hostReadPath=/opt/ibm/wlp \
#    --hostWritePath=/opt/ibm/wlp

# [ -d  /config/resources/collective ] || /usr/bin/expect /config/join-collective.expect

# start the defaultServer in the background needs to connect to properly register itself to the controller.
/opt/ibm/wlp/bin/server run defaultServer &

# Use sshd as the "daemon" process
/usr/sbin/sshd -D &
wait
