k: clean build-push k-refresh k-logs

clean:
	mvn clean
build:
	quarkus build -Dquarkus.container-image.build=true
build-push:
	quarkus build -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
run:
	quarkus dev
run-docker:
	docker run -ti \
	--name code-snippet \
	--rm=true \
	-e QUARKUS_LOG_LEVEL=INFO \
	-e QUARKUS_HTTP_PORT=8080 \
	-e QUARKUS_DATASOURCE_JDBC_URL=jdbc:mysql://152.228.214.216:6871/snippets \
	-e QUARKUS_DATASOURCE_PASSWORD=xi5Jai6aelai \
	-e QUARKUS_DATASOURCE_USERNAME=snippet \
	-e MP_JWT_VERIFY_PUBLICKEY="$(shell cat /home/nherbaut/workspace/java-runner-analytics/keys/publicKey.pem |tr -d '\n')" \
	-p 8077:8080 \
	nherbaut/code-snippet:1.0.0-SNAPSHOT

k-refresh:
	kubectl delete pods -l app=code-snippet && sleep 10

k-logs:
	kubectl logs -l app=code-snippet -f
	
