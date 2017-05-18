docker stack rm jeesample
timeout /T 10
docker stack deploy --compose-file docker-compose.yml jeesample
