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

# Generate key stores for Dynamic Routing use shorter password because of format issues.
PASSWORD=$(openssl rand -hex 16)
echo $PASSWORD
/opt/ibm/wlp/bin/dynamicRouting genKeystore \
  --host=controller \
  --user=adminUser \
  --password=adminPassword \
  --port=9443 \
  --keystorePassword="$PASSWORD" \
  --autoAcceptCertificates

# /opt/ibm/wlp/bin/dynamicRouting genKeystore   --host=controller   --user=adminUser   --password=adminPassword   --port=9443   --keystorePassword=foofoo   --autoAcceptCertificates

# +CXlp+VS1yOKDqzSp21Jtd1RWfX4cM5Bh1UKfqNA2HU=
# Convert to .p12 first to make it work with gskcapicmd
mkdir -p /opt/IBM/WebSphere/Plugins/config/webserver1
# /opt/IBM/HTTPServer/bin/gskcmd -keydb -convert \
#   -pw "$PASSWORD" \
#   -db /plugin-key.jks \
#   -target /tmp/plugin-key.p12 \
#   -new_format p12 
# /opt/IBM/HTTPServer/bin/gskcapicmd -keydb -convert \
#   -pw "$PASSWORD" \
#   -db /tmp/plugin-key.p12 \
#   -target /opt/IBM/WebSphere/Plugins/config/webserver1/plugin-key.kdb \
#   -new_format cms
# /opt/IBM/HTTPServer/bin/gskcapicmd -cert -setdefault \
#   -pw "$PASSWORD" \
#   -db /opt/IBM/WebSphere/Plugins/config/webserver1/plugin-key.kdb \
#   -label default

cat /opt/IBM/HTTPServer/conf/java.security.append >> /opt/IBM/HTTPServer/java/8.0/jre/lib/security/java.security
gskcmd -keydb -convert \
  -pw "$PASSWORD" \
  -db /plugin-key.jks \
  -target /opt/IBM/WebSphere/Plugins/config/webserver1/plugin-key.kdb \
  -new_format cms \
  -stash && \
gskcmd -cert -setdefault \
  -stashed \
  -db /opt/IBM/WebSphere/Plugins/config/webserver1/plugin-key.kdb \
  -label default


mkdir -p /opt/IBM/WebSphere/Plugins/logs/webserver1
#mv /tmp/plugin-key.* /opt/IBM/WebSphere/Plugins/config/webserver1

exec /work/ihsstart.sh
