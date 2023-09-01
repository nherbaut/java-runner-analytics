k: build-push k-refresh k-logs

build:
	quarkus build -Dquarkus.container-image.build=true
build-push:
	quarkus build -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
run:
	quarkus dev
run-docker:
	docker run -ti \
	--name event-sink \
	--rm=true \
	-e QUARKUS_LOG_LEVEL=TRACE \
	-e QUARKUS_HTTP_PORT=8080 \
	-e RABBITMQ_HOST=localhost \
	-e RABBITMQ_PORT=5672 \
	-e RABBITMQ_USERNAME=admin \
	-e RABBITMQ_PASSWORD=admin \
	-e MP_JWT_VERIFY_PUBLICKEY="$(shell cat /home/nherbaut/workspace/java-runner-analytics/keys/publicKey.pem |tr -d '\n')" \
	-p 8077:8080 \
	nherbaut/event-sink:1.0.0-SNAPSHOT

k-refresh:
	kubectl delete pods -l app=event-sink && sleep 10

k-logs:
	kubectl logs -l app=event-sink -f
	
