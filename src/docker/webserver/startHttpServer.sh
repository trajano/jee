#!/bin/bash
# check for SSL keys
if [ ! -e /share/selfsigned.kdb ]
then
  ( 
    flock 200
    /opt/IBM/HTTPServer/bin/gskcapicmd -keydb -create -db /share/selfsigned.kdb -stash -genpw
    /opt/IBM/HTTPServer/bin/gskcapicmd -cert \
        -create \
        -db /share/selfsigned.kdb \
        -label default \
        -size 2048 \
        -dn "CN=noriko, O=Trajano, C=CA" \
        -default_cert yes \
        -expire 365 \
        -sigalg sha512 \
        -stashed
  ) 200> /share/gskapicmd.lock
fi

# wait for controller to be up
while ! nc -z controller 9443; do
  sleep 0.1 # wait for 1/10 of the second before check again
done
flock /share/pluginUtility.lock -c "/opt/ibm/wlp/bin/pluginUtility generate  --server=adminUser:adminPassword@controller:9443  --cluster=defaultCluster --targetPath=/share"
exec /work/ihsstart.sh
