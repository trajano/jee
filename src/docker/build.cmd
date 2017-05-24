docker build -t controller controller && docker tag controller trajano/jee-controller:base && docker push trajano/jee-controller:base
rem docker build -t controller.replica controller.replica && docker tag controller.replica trajano/jee-controller:replica && docker push trajano/jee-controller:replica
docker build -t trajano/jee-appserver:base appserver.base && docker tag trajano/jee-appserver:base trajano/jee-appserver:base && docker push trajano/jee-appserver:base

rem docker build -t trajano/liberty-host:base host && docker tag trajano/liberty-host:base trajano/liberty-host:base && docker push trajano/liberty-host:base

rem docker build -t trajano/jee-webserver:base webserver && docker tag trajano/jee-appserver:base trajano/jee-appserver:base && docker push trajano/jee-webserver:base
