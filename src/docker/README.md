docker stack rm jeesample
docker stack  deploy --compose-file docker-compose.yml jeesample

The controller replica may need to have a separate server so there's one "primary" which has the `volume` and the rest will pull from the core one.


pluginUtility generate  --server=adminUser:adminPassword@controller:9443  --cluster=defaultCluster

cat /defaultCluster-plugin-cfg.xml

docker exec 82246b134e6b collective unregisterHost 3643a3abdfdf --user=adminUser --password=adminPassword --port=9443 --host=controller --autoAcceptCertificates

# Notes

* May need to change the appserver.base to just have SSH and WLP only do not start and then use autoscale to provision the server.
* There should be no need for installables it should be expected that WLP and JRE are already present in the target node.

D:\WLIB\bin\server package deployable --include=usr 
move D:\WLIB\usr\servers\deployable\deployable.zip D:\p\jee\src\docker

docker cp deployable.zip 98eac34a77af:/opt/ibm/wlp/usr/shared/stackGroups/defaultStackGroup/packages

# TODO 

* Replica Sets for the controller