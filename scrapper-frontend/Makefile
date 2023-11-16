k: clean build-push k-refresh k-logs

clean:
	mvn clean
build: clean
	quarkus build -Dquarkus.container-image.build=true
build-push:
	quarkus build -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
run:
	quarkus dev
run-docker:
	docker run -ti \
	--name scrapper \
	--rm=true \
	-e QUARKUS_LOG_LEVEL=INFO \
	-e QUARKUS_HTTP_PORT=8080 \
	-e QUARKUS_REST_CLIENT_SCRAPPING_FUNCTION_URL=http://localhost:8090/function
	-p 8077:8080 \
	nherbaut/scrapper:1.0.0-SNAPSHOT

k-refresh:
	kubectl delete pods -l app=scrapper && sleep 10

k-logs:
	kubectl logs -l app=scrapper -f
	
