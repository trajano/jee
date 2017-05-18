rem docker build -t controller controller
rem docker tag controller trajano/jee-controller:base
rem docker push trajano/jee-controller:base

docker build -t appserver appserver
docker tag appserver trajano/jee-appserver:withapp
rem docker push trajano/jee-appserver:base
