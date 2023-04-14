
jar:
	mvn clean install

run:
	mvn exec:java

docker-up:
	docker-compose -f docker-compose.yaml up --build

docker-down: ## Stop docker containers and clear artefacts.
	docker-compose -f docker-compose.yaml down
	docker system prune
