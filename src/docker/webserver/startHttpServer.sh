#!/bin/bash
# check for SSL keys
if [ ! -e /share/selfsigned.kdb ]
then
  ( 
    flock 200
    gskcmd -keydb -create -db /share/selfsigned.kdb -stash -genpw
    gskcmd -cert \
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

# The following sets up dynamic routing but it is not used until 9.0.0.3 docker image is released.  Remove from after fi
PASSWORD=$(openssl rand -hex 16)
export JAVA_HOME=/opt/IBM/HTTPServer/java/8.0/jre
while ! /opt/ibm/wlp/bin/dynamicRouting genKeystore \
    --host=controller \
    --user=adminUser \
    --password=adminPassword \
    --port=9443 \
    --keystorePassword="$PASSWORD" \
    --autoAcceptCertificates
do
  sleep 1
done
unset JAVA_HOME

gskcmd -keydb -convert \
  -db /plugin-key.jks \
  -pw $PASSWORD \
  -new_format cms \
  -stash
gskcmd -cert -setdefault \
  -label default \
  -db /plugin-key.kdb \
  -stashed

. /opt/IBM/HTTPServer/bin/envvars
exec /opt/IBM/HTTPServer/bin/httpd -d /opt/IBM/HTTPServer -DFOREGROUND
