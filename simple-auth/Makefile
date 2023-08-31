k: build-push k-refresh k-logs

build:
	quarkus build -Dquarkus.container-image.build=true
build-push:
	quarkus build -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true
run:
	quarkus dev
run-docker:
	docker run -ti \
	--name simple-auth \
	--rm=true \
	-e QUARKUS_LOG_LEVEL=TRACE \
	-e QUARKUS_HTTP_PORT=8080 \
	-e RECAPTCHA_SITE_KEY=6Ld_HOQnAAAAAPD_SWkfULsuD_onIvKs254svp1n \
	-e GOOGLE_API_KEY=AIzaSyBRDTM-xnl2JegnPLaXAf3ekSvaYz2xtkw \
	-e SMALLRYE_JWT_SIGN_KEY="$(shell cat src/main/resources/crypto/privateKey.pem|tr -d "\n")" \
	-e MP_JWT_VERIFY_PUBLICKEY="$(shell cat src/main/resources/crypto/publicKey.pem |tr -d '\n')" \
	-p 8079:8080 \
	nherbaut/simple-auth:1.0.0-SNAPSHOT

k-refresh:
	kubectl delete pods -l app=simple-auth && sleep 10

k-logs:
	kubectl logs -l app=simple-auth -f
	
