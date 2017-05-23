#!/bin/bash
# check for SSL keys
if [ ! -e /share/selfsigned.kdb ]
then
  ( 
    flock 200
    gskcapicmd -keydb -create -db /share/selfsigned.kdb -stash -genpw
    gskcapicmd -cert \
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

mkdir -p /opt/IBM/WebSphere/Plugins/config/webserver1
gskcapicmd -keydb -create -db /opt/IBM/WebSphere/Plugins/config/webserver1/plugin-key.kdb -genpw -stash
# Keep on trying until the configuration file is generated
while [ ! -e /defaultCluster-plugin-cfg.xml ]
do
  sleep 0.1 # wait for 1/10 of the second before check again
  /opt/ibm/wlp/bin/pluginUtility generate \
    --server=adminUser:adminPassword@controller:9443 \
    --cluster=defaultCluster
done

# Manipulate text to extract the host names of the cluster
for memberHost in $( grep "9443" /defaultCluster-plugin-cfg.xml | sed -n 's/^.*Hostname="\([0-9a-f]*\)".*/\1/p' )
do
  echo QUIT | openssl s_client -showcerts -connect $memberHost:9443 > $memberHost.pem
  gskcapicmd -cert -import \
    -type p12 \
    -file $memberHost.pem \
    -target /opt/IBM/WebSphere/Plugins/config/webserver1/plugin-key.kdb \
    -target_stashed
done

# Remove the administrative URIs from the configuration
sed -i 's#<Uri AffinityCookie="JSESSIONID" AffinityURLIdentifier="jsessionid" Name="/IBMJMXConnectorREST/\*"/>##' /defaultCluster-plugin-cfg.xml
sed -i 's#<Uri AffinityCookie="JSESSIONID" AffinityURLIdentifier="jsessionid" Name="/ibm/api/\*"/>##' /defaultCluster-plugin-cfg.xml

# The following sets up dynamic routing but it is not used until 9.0.0.3 docker image is released.  Remove from after fi
PASSWORD=$(openssl rand -hex 16)

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

# needs 9.0.0.3 to work
# gskcmd -keydb -convert \
#   -db /plugin-key.jks \
#   -pw $PASSWORD \
#   -target /plugin-key.kdb \
#   -new_format cms \
#   -stash
# gskcmd -cert -setdefault \
#   -label default \
#   -db /plugin-key.kdb \
#   -stashed

# Make directory used for the plugin logs
mkdir -p /opt/IBM/WebSphere/Plugins/logs/webserver1

#exec /work/ihsstart.sh
. /opt/IBM/HTTPServer/bin/envvars
exec /opt/IBM/HTTPServer/bin/httpd -d /opt/IBM/HTTPServer -DFOREGROUND

