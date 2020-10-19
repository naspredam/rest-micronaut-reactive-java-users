start:
	docker build -t rest-micronaut-java-users .
	docker-compose up -d

stop:
	docker-compose down

restart:
	make stop && make start

logs:
	docker-compose logs -f