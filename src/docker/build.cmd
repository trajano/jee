docker build -t controller controller
docker tag controller trajano/jee-controller:base
docker push trajano/jee-controller:base

docker build -t trajano/jee-appserver:base appserver.base
docker tag trajano/jee-appserver:base trajano/jee-appserver:base
docker push trajano/jee-appserver:base

rem docker build -t trajano/jee-appserver:withapp appserver
rem docker tag appserver trajano/jee-appserver:withapp
rem docker push trajano/jee-appserver:withapp

rem docker build -t trajano/jee-webserver:base webserver
rem docker tag trajano/jee-appserver:base trajano/jee-appserver:base
rem docker push trajano/jee-webserver:base
