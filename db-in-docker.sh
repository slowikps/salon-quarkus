if docker ps -a | grep salon-db; then
  docker start salon-db
else
  docker run -p 127.0.0.1:6677:5432 --name salon-db -e POSTGRES_PASSWORD=pass -e POSTGRES_USER=usr -d postgres:12.1
fi
